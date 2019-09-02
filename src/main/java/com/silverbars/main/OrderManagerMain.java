package com.silverbars.main;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.silverbars.manager.OrderManagerFactory;
import com.silverbars.manager.OrderManagerI;
import com.silverbars.order.Order;
import com.silverbars.order.OrderCurrency;
import com.silverbars.order.OrderType;
import com.silverbars.order.OrderUnit;

/**
 * This simulates many clients to test out the concurrency of the orderSet and the orderSummaryMap
 * @author ben
 *
 */
public class OrderManagerMain {
	static Random rnd = new Random();

	public static void main(String[] args) throws InterruptedException {

		ExecutorService executorServiceAdd = Executors.newFixedThreadPool(100);

		final OrderManagerI orderManager = OrderManagerFactory.getDefaultOrderManager();

		Runnable runnableAddTask = () -> {
			Order order = new Order.Builder()
					.userId("user" + rnd.nextInt(10))
					.quantity(new BigDecimal(Integer.toString(rnd.nextInt(99)+1) + "." + Integer.toString(rnd.nextInt(100))))
					.orderUnit(getRandomOrderUnit())
					.orderType(getRandomOrderType())
					.orderCurrency(getRandomOrderCurrency())
					.orderPrice(new BigDecimal(Integer.toString(rnd.nextInt(9999)+1) + "." + Integer.toString(rnd.nextInt(100))))
					.build();
			orderManager.registerOrder(order);
		};
		for(int i=0;i<1_000_000;i++) {
			executorServiceAdd.execute(runnableAddTask);
		}
		
		
		executorServiceAdd.shutdown();
		executorServiceAdd.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		System.out.println("should be 1000000: " + orderManager.getOrders().size() + "...implies thread safe");
		System.out.println("should be nearly 1000000, save for the merged Orders: " + orderManager.getOrderSummary().size());

		ExecutorService executorServiceAddAndCancel = Executors.newFixedThreadPool(100);

		OrderManagerFactory.resetOrderManager();
		OrderManagerI orderManager2 = OrderManagerFactory.getDefaultOrderManager();

		Runnable runnableAddAndCancelTask = () -> {
			Order order = new Order.Builder()
					.userId("user" + rnd.nextInt(10))
					.quantity(new BigDecimal(Integer.toString(rnd.nextInt(99)+1) + "." + Integer.toString(rnd.nextInt(100))))
					.orderUnit(getRandomOrderUnit())
					.orderType(getRandomOrderType())
					.orderCurrency(getRandomOrderCurrency())
					.orderPrice(new BigDecimal(Integer.toString(rnd.nextInt(9999)+1) + "." + Integer.toString(rnd.nextInt(100))))
					.build();
			orderManager2.registerOrder(order);
			orderManager2.cancelOrder(order);
			
		};
		for(int i=0;i<1_000_000;i++) {
			executorServiceAddAndCancel.execute(runnableAddAndCancelTask);
		}
		
		
		executorServiceAddAndCancel.shutdown();
		executorServiceAddAndCancel.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

		System.out.println("should be 0: " + orderManager2.getOrders().size() + "...implies thread safe");
		System.out.println("should be 0: " + orderManager2.getOrderSummary().size() + "...implies thread safe");

	}

	private static OrderCurrency getRandomOrderCurrency() {
		if(rnd.nextBoolean()) {
			return OrderCurrency.USD;
		}
		return OrderCurrency.GBP;
	}

	private static OrderType getRandomOrderType() {
		if(rnd.nextBoolean()) {
			return OrderType.BUY;
		}
		return OrderType.SELL;
	}

	private static OrderUnit getRandomOrderUnit() {
		if(rnd.nextBoolean()) {
			return OrderUnit.KG;
		}
		return OrderUnit.LITRE;
	}
}
