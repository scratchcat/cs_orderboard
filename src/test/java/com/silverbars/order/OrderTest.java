package com.silverbars.order;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.silverbars.order.Order;

public class OrderTest {

	@Before
	public void clear() {
		OrderUtil.resetOrderId();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOrderBuilderBuildBasic() {
		Order build = new Order.Builder().build();	    	
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOrderIdBasic() {
		Order order = new Order.Builder().build();
		long orderId = order.getOrderId();
		assertEquals(1l, orderId);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOrderIdMoreThanOneOrder() {
		Order order1 = new Order.Builder().build();
		Order order2 = new Order.Builder().build();

		long order1Id = order1.getOrderId();
		long order2Id = order2.getOrderId();

		assertEquals(1l, order1Id);
		assertEquals(2l, order2Id);
	}

	@Test
	public void testOrderBuilderWithDetailsOne() {
		Order order = new Order.Builder()
				.userId("user1")
				.quantity(new BigDecimal("3.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();
		assertEquals(1, order.getOrderId());
		assertEquals("user1", order.getUserId());
		assertEquals(new BigDecimal("3.5"), order.getQuantity());
		assertEquals(OrderUnit.KG, order.getOrderUnit());
		assertEquals(OrderType.SELL, order.getOrderType());
		assertEquals(OrderCurrency.GBP, order.getOrderCurrency());
		assertEquals(new BigDecimal("306"), order.getOrderPrice());
	}


	@Test
	public void testOrderBuilderWithDetailsMultiple() {
		Order order1 = new Order.Builder()
				.userId("user1")
				.quantity(new BigDecimal("3.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();

		Order order2 = new Order.Builder()
				.userId("user2")
				.quantity(new BigDecimal("1.2"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("310"))
				.build();

		Order order3 = new Order.Builder()
				.userId("user3")
				.quantity(new BigDecimal("1.5"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("307"))
				.build();

		Order order4 = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.KG)
				.orderType(OrderType.SELL)
				.orderCurrency(OrderCurrency.GBP)
				.orderPrice(new BigDecimal("306"))
				.build();


		assertEquals(1, order1.getOrderId());
		assertEquals("user1", order1.getUserId());
		assertEquals(new BigDecimal("3.5"), order1.getQuantity());
		assertEquals(OrderUnit.KG, order1.getOrderUnit());
		assertEquals(OrderType.SELL, order1.getOrderType());
		assertEquals(OrderCurrency.GBP, order1.getOrderCurrency());
		assertEquals(new BigDecimal("306"), order1.getOrderPrice());

		assertEquals(2, order2.getOrderId());
		assertEquals("user2", order2.getUserId());
		assertEquals(new BigDecimal("1.2"), order2.getQuantity());
		assertEquals(OrderUnit.KG, order2.getOrderUnit());
		assertEquals(OrderType.SELL, order2.getOrderType());
		assertEquals(OrderCurrency.GBP, order2.getOrderCurrency());
		assertEquals(new BigDecimal("310"), order2.getOrderPrice());

		assertEquals(3, order3.getOrderId());
		assertEquals("user3", order3.getUserId());
		assertEquals(new BigDecimal("1.5"), order3.getQuantity());
		assertEquals(OrderUnit.KG, order3.getOrderUnit());
		assertEquals(OrderType.SELL, order3.getOrderType());
		assertEquals(OrderCurrency.GBP, order3.getOrderCurrency());
		assertEquals(new BigDecimal("307"), order3.getOrderPrice());

		assertEquals(4, order4.getOrderId());
		assertEquals("user4", order4.getUserId());
		assertEquals(new BigDecimal("2.0"), order4.getQuantity());
		assertEquals(OrderUnit.KG, order4.getOrderUnit());
		assertEquals(OrderType.SELL, order4.getOrderType());
		assertEquals(OrderCurrency.GBP, order4.getOrderCurrency());
		assertEquals(new BigDecimal("306"), order4.getOrderPrice());
	}

	@Test
	public void testOrderBuyLitreUsd() {
		Order order1 = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();

		assertEquals(OrderType.BUY, order1.getOrderType());
		assertEquals(OrderUnit.LITRE, order1.getOrderUnit());
		assertEquals(OrderCurrency.USD, order1.getOrderCurrency());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyUserAttribute() {
		Order missingUser = new Order.Builder()
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyQuantityAttribute() {
		Order missingQuantity = new Order.Builder()
				.userId("user4")
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyOrderUnitAttribute() {
		Order missingOrderUnit = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyOrderTypeAttribute() {
		Order missingOrderType = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderCurrency(OrderCurrency.USD)
				.orderPrice(new BigDecimal("306"))
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyOrderCurrencyAttribute() {
		Order missingOrderCurrency = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderPrice(new BigDecimal("306"))
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyOrderPriceAttribute() {
		Order missingOrderPrice = new Order.Builder()
				.userId("user4")
				.quantity(new BigDecimal("2.0"))
				.orderUnit(OrderUnit.LITRE)
				.orderType(OrderType.BUY)
				.orderCurrency(OrderCurrency.USD)
				.build();
	}
}
