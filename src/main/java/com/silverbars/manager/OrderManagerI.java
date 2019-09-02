package com.silverbars.manager;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.silverbars.order.Order;

public interface OrderManagerI {

	public void registerOrder(Order order);

	public boolean existsOrder(Order order);

	public void cancelOrder(Order order);

	public Map<MergeKey, BigDecimal> getOrderSummary();

	public Comparator getComparator();

	public Set<Order> getOrders();

}
