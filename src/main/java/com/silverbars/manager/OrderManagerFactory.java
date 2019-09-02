package com.silverbars.manager;

public class OrderManagerFactory {
	
	private static OrderManagerI orderManager = new OrderManager();
	
	public static OrderManagerI getDefaultOrderManager() {
		return orderManager;
	}

	public static void resetOrderManager() {
		orderManager = new OrderManager();
	}

}
