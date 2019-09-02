package com.silverbars.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.silverbars.order.OrderCurrency;
import com.silverbars.order.OrderTest;
import com.silverbars.order.OrderType;
import com.silverbars.order.OrderUnit;

public class MergeKeyTest {

	@Test
	public void testConstructor() {
		MergeKey mergeKey = new MergeKey(OrderManagerTest.getOrder1Sell());
		assertEquals(OrderCurrency.GBP,mergeKey.getOrderCurrency());
		assertEquals(new BigDecimal("306"),mergeKey.getOrderPrice());
		assertEquals(OrderType.SELL,mergeKey.getOrderType());
		assertEquals(OrderUnit.KG,mergeKey.getOrderUnit());
	}
	
	@Test
	public void testSetAddingAndGetting() {
		MergeKey mergeKey = new MergeKey(OrderManagerTest.getOrder1Sell());
		MergeKey mergeKey2 = new MergeKey(OrderManagerTest.getOrder1Sell());
		
		Set<MergeKey> set = new HashSet<>();
		set.add(mergeKey);
		assertTrue(set.contains(mergeKey2));
	}
}
