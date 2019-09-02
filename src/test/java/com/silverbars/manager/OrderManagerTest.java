package com.silverbars.manager;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.silverbars.order.Order;
import com.silverbars.order.OrderCurrency;
import com.silverbars.order.OrderType;
import com.silverbars.order.OrderUnit;


public class OrderManagerTest {

	@Before
	public void clear() {
		OrderManagerFactory.resetOrderManager();
	}
		
	@Test
	public void testCreateOrderManagerBasic() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		assertNotNull(orderManager);
	}

	@Test
	public void testRegisterOrderOne() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		orderManager.registerOrder(order1Sell);
		assertTrue(orderManager.existsOrder(order1Sell));
		assertNotNull(orderManager.getOrders());
		Set<Order> orders = orderManager.getOrders();
		assertEquals(order1Sell, new ArrayList<Order>(orders).get(0));
	}

	@Test
	public void testRegisterOrderMultiple() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		Order order2Sell = getOrder2Sell();
		Order order3Sell = getOrder3Sell();
		Order order4Sell = getOrder4Sell();
		Order order1Buy = getOrder1Buy();
		Order order2Buy = getOrder2Buy();
		Order order3Buy = getOrder3Buy();
		Order order4Buy = getOrder4Buy();
		orderManager.registerOrder(order1Sell);
		orderManager.registerOrder(order2Sell);
		orderManager.registerOrder(order3Sell);
		orderManager.registerOrder(order4Sell);
		orderManager.registerOrder(order1Buy);
		orderManager.registerOrder(order2Buy);
		orderManager.registerOrder(order3Buy);
		orderManager.registerOrder(order4Buy);
		assertTrue(orderManager.existsOrder(order1Sell));
		assertTrue(orderManager.existsOrder(order2Sell));
		assertTrue(orderManager.existsOrder(order3Sell));
		assertTrue(orderManager.existsOrder(order4Sell));
		assertTrue(orderManager.existsOrder(order1Buy));
		assertTrue(orderManager.existsOrder(order2Buy));
		assertTrue(orderManager.existsOrder(order3Buy));
		assertTrue(orderManager.existsOrder(order4Buy));
	}

	@Test
	public void testRegisterOrderAndCancelMultiple() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		Order order2Sell = getOrder2Sell();
		Order order1Buy = getOrder1Buy();
		Order order2Buy = getOrder2Buy();
		orderManager.registerOrder(order1Sell);
		orderManager.registerOrder(order2Sell);
		orderManager.registerOrder(order1Buy);
		orderManager.registerOrder(order2Buy);
		assertTrue(orderManager.existsOrder(order1Sell));
		assertTrue(orderManager.existsOrder(order2Sell));
		assertTrue(orderManager.existsOrder(order1Buy));
		assertTrue(orderManager.existsOrder(order2Buy));
		
		orderManager.cancelOrder(order1Sell);
		orderManager.cancelOrder(order2Sell);
		orderManager.cancelOrder(order1Buy);
		orderManager.cancelOrder(order2Buy);
		
		assertFalse(orderManager.existsOrder(order1Sell));
		assertFalse(orderManager.existsOrder(order2Sell));
		assertFalse(orderManager.existsOrder(order1Buy));
		assertFalse(orderManager.existsOrder(order2Buy));
	}
	
	@Test
	public void testOrderSummaryBasic() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		Order order2Sell = getOrder2Sell();
		Order order3Sell = getOrder3Sell();
		Order order4Sell = getOrder4Sell();
		
		orderManager.registerOrder(order1Sell);
		orderManager.registerOrder(order2Sell);
		orderManager.registerOrder(order3Sell);
		orderManager.registerOrder(order4Sell);
		
		Map<MergeKey,BigDecimal> summaryMap = orderManager.getOrderSummary();
				
		assertEquals(new BigDecimal("5.5"),summaryMap.get(new MergeKey(order4Sell)));
		assertEquals(new BigDecimal("1.2"),summaryMap.get(new MergeKey(order2Sell)));
		assertEquals(new BigDecimal("1.5"),summaryMap.get(new MergeKey(order3Sell)));	
	
		orderManager.cancelOrder(order1Sell);
		orderManager.cancelOrder(order2Sell);
		orderManager.cancelOrder(order3Sell);
		assertEquals(new BigDecimal("2.0"),summaryMap.get(new MergeKey(order4Sell)));
		assertNull(summaryMap.get(new MergeKey(order2Sell)));
		assertNull(summaryMap.get(new MergeKey(order3Sell)));	

	}

	@Test
	public void testOrderSummaryComparatorOne() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		Order order2Sell = getOrder2Sell();
		Order order3Sell = getOrder3Sell();
		Order order4Sell = getOrder4Sell();
		Order order1Buy = getOrder1Buy();
		Order order2Buy = getOrder2Buy();
		Order order3Buy = getOrder3Buy();
		Order order4Buy = getOrder4Buy();
		orderManager.registerOrder(order1Sell);
		orderManager.registerOrder(order2Sell);
		orderManager.registerOrder(order3Sell);
		orderManager.registerOrder(order4Sell);
		orderManager.registerOrder(order1Buy);
		orderManager.registerOrder(order2Buy);
		orderManager.registerOrder(order3Buy);
		orderManager.registerOrder(order4Buy);

		List<MergeKey> list = new ArrayList(orderManager.getOrderSummary().keySet());
		Collections.reverse(list);
		Collections.sort(list, orderManager.getComparator());
		
		boolean isBuy = false;
		
		//test sells before buys in the ordersummarymap
		for (MergeKey mergeKey : list) {
			if(mergeKey.getOrderType()==OrderType.BUY) {
				isBuy = true;
			}
			
			if(isBuy==true && mergeKey.getOrderType()==OrderType.SELL) {
				fail("SELLs should appear before BUYs");
			}
		}

		BigDecimal lastPriceSell = null;
		BigDecimal lastPriceBuy = null;
		
		//tests ascending sell prices and descending buy prices in the summary map
		for (MergeKey mergeKey : list) {
			if(mergeKey.getOrderType()==OrderType.SELL) {
				BigDecimal orderPrice = mergeKey.getOrderPrice();
				if(lastPriceSell==null) {
					lastPriceSell = orderPrice;
				}
				else {
					assertTrue(orderPrice.compareTo(lastPriceSell)>=0);
				}
			}
			if(mergeKey.getOrderType()==OrderType.BUY) {
				BigDecimal orderPrice = mergeKey.getOrderPrice();
				if(lastPriceBuy==null) {
					lastPriceBuy = orderPrice;
				}
				else {
					assertTrue(orderPrice.compareTo(lastPriceBuy)<=0);
				}
			}
		}
	}

	@Test
	public void testOrderSummaryDifferentUnitsAndCurrency() {
		OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();
		Order order1Sell = getOrder1Sell();
		Order order2Sell = getOrder2Sell();
		Order order3Sell = getOrder3Sell();
		Order order4Sell = getOrder4Sell();
		Order order5Sell = getOrder5Sell();
		Order order6Sell = getOrder6Sell();
		Order order1Buy = getOrder1Buy();
		Order order2Buy = getOrder2Buy();
		Order order3Buy = getOrder3Buy();
		Order order4Buy = getOrder4Buy();
		Order order5Buy = getOrder5Buy();
		Order order6Buy = getOrder6Buy();
		Order order7Buy = getOrder7Buy();
		orderManager.registerOrder(order1Sell);
		orderManager.registerOrder(order2Sell);
		orderManager.registerOrder(order3Sell);
		orderManager.registerOrder(order4Sell);
		orderManager.registerOrder(order5Sell);
		orderManager.registerOrder(order6Sell);
		orderManager.registerOrder(order1Buy);
		orderManager.registerOrder(order2Buy);
		orderManager.registerOrder(order3Buy);
		orderManager.registerOrder(order4Buy);
		orderManager.registerOrder(order5Buy);
		orderManager.registerOrder(order6Buy);
		orderManager.registerOrder(order7Buy);

		Map<MergeKey, BigDecimal> summaryMap = orderManager.getOrderSummary();

		assertEquals(new BigDecimal("5.5"),summaryMap.get(new MergeKey(order4Sell)));
		assertEquals(new BigDecimal("1.2"),summaryMap.get(new MergeKey(order2Sell)));
		assertEquals(new BigDecimal("1.5"),summaryMap.get(new MergeKey(order3Sell)));	
	
		assertEquals(new BigDecimal("1.0"),summaryMap.get(new MergeKey(order5Buy)));	
		assertEquals(new BigDecimal("4.8"),summaryMap.get(new MergeKey(order7Buy)));
		
		int size = summaryMap.size();
		int counter = 0;
		for (MergeKey mergeKey : summaryMap.keySet()) {
			if(counter==1) {
				assertEquals(OrderCurrency.USD, mergeKey.getOrderCurrency());
			}
			if(counter==3) {
				assertEquals(OrderCurrency.USD, mergeKey.getOrderCurrency());
				assertEquals(OrderUnit.LITRE, mergeKey.getOrderUnit());
			}
			if(counter==size-2) {
				assertEquals(OrderCurrency.USD, mergeKey.getOrderCurrency());
			}
			if(counter==size-1) {
				assertEquals(OrderUnit.LITRE, mergeKey.getOrderUnit());
			}
			counter++;
		}
	}
	
	public static Order getOrder1Sell() {
		return new Order.Builder()
				.userId("user1")
				.quantity(new BigDecimal("3.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();		
	}

	public static Order getOrder2Sell() {
		return new Order.Builder()
				.userId("user2")
				.quantity(new BigDecimal("1.2"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("310"))
				.build();
	}

	public static Order getOrder3Sell() {
		return new Order.Builder()
				.userId("user3")
				.quantity(new BigDecimal("1.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("307"))
				.build();
	}
	
	public static Order getOrder4Sell() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	private Order getOrder5Sell() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	private Order getOrder6Sell() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("308"))
				.build();
	}

	
	public static Order getOrder1Buy() {
		return new Order.Builder()
				.userId("user1")
				.quantity(new BigDecimal("3.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();		
	}

	public static Order getOrder2Buy() {
		return new Order.Builder()
				.userId("user2")
				.quantity(new BigDecimal("1.2"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("310"))
				.build();
	}

	public static Order getOrder3Buy() {
		return new Order.Builder()
				.userId("user3")
				.quantity(new BigDecimal("1.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("307"))
				.build();
	}
	
	public static Order getOrder4Buy() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	public static Order getOrder5Buy() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("1.0"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	public static Order getOrder6Buy() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	public static Order getOrder7Buy() {
		return new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.8"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();
	}
	
	
}
