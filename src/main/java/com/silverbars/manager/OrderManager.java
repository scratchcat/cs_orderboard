package com.silverbars.manager;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.silverbars.order.Order;
import com.silverbars.order.OrderType;

public class OrderManager implements OrderManagerI {

	//HashSet is not thread safe. As we have one OrderManager, 
	//assume different clients will be adding/removing orders simultaneously.
	//Set is quicker than a list. Ordering is not needed, just thread-safety.
	Set<Order> orderSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
	
	Comparator<MergeKey> comparator = new Comparator<MergeKey>() {
		public int compare(MergeKey mk1, MergeKey mk2) {
			int compareOrderType = mk1.getOrderType().compareTo(mk2.getOrderType());

			//I decided to sort by (and so allow) different units and currencies, given they are part of the mergeKey
			if(compareOrderType==0) {
				int compareOrderPrice = mk1.getOrderPrice().compareTo(mk2.getOrderPrice());
				if(compareOrderPrice==0) {
					int compareOrderUnit = mk1.getOrderUnit().compareTo(mk2.getOrderUnit());
					if(compareOrderUnit==0) {
						int compareOrderCurrency = mk1.getOrderCurrency().compareTo(mk2.getOrderCurrency());
						return compareOrderCurrency;
					}
					return compareOrderUnit;
				}
				if(mk1.getOrderType()==OrderType.BUY) {
					return compareOrderPrice*-1;
				}
				return compareOrderPrice;
			}
			return compareOrderType;
		}	
	};

	//provides a key sortable, thread-safe and quick map
	private Map<MergeKey, BigDecimal> orderSummaryMap = new ConcurrentSkipListMap<>(comparator);


	@Override
	public void registerOrder(Order order) {
		boolean added = this.orderSet.add(order);

		if(added) { 		
			this.orderSummaryMap.compute(new MergeKey(order), 
					(key,val)->(val==null)? order.getQuantity() : val.add(order.getQuantity()));		
		}
	}

	@Override
	public boolean existsOrder(Order order) {
		return this.orderSet.contains(order);
	}

	@Override
	public void cancelOrder(Order order) {
		boolean removed = this.orderSet.remove(order);
		if(removed) {
			this.orderSummaryMap.compute(new MergeKey(order), 
					(key,val)->(val.subtract(order.getQuantity())));
			
			//remove 0 summaries as they shouldn't be reported
			if(new BigDecimal("0").compareTo(orderSummaryMap.get(new MergeKey(order)))==0) {
				this.orderSummaryMap.remove(new MergeKey(order));
			}
		}
	}

	/**
	 * Should getOrderSummary be generated from the orderSet afresh, each time it is called, or should
	 * it be maintained piecemeal, as orders are registered or cancelled?
	 * To be thread safe, generating afresh would mean that the order set would need to be synchronized,
	 * which implies order registration would need to be stopped for a time. This feels like the wrong approach,
	 * as order registration should be prioritised. However, the approach taken on reporting of orders (which is
	 * what the summary is), should vary, dependent on the frequency of request. For example, a once a day summary,
	 * generated at the end of day, would could reasonably synchronise on the order set. However, if order summary
	 * is called frequently, by many clients, then it makes sense instead to maintain this during order registration 
	 * and cancellation as it would have very little runtime impact. Ideal it would be a ConcurrentHashMap, 
	 * but as it needs to be order, returning a sorted TreeMap derived from the ConcurrentHashMap, would also lock 
	 * the summaryMap (in the case that the summary needs to be fully consistent and correct). Hence, I have chosen 
	 * to use a ConcurrentSkipListMap which enables map manipulation without much waiting. 
	 */
	@Override
	public Map<MergeKey, BigDecimal> getOrderSummary() {
		return this.orderSummaryMap;
	}

	@Override
	public Comparator<MergeKey> getComparator() {
		return this.comparator;
	}

	@Override
	public Set<Order> getOrders() {
		return this.orderSet;
	}


}
