package com.silverbars.order;

import java.util.concurrent.atomic.AtomicLong;

public class OrderUtil {
	static AtomicLong atomicLong = new AtomicLong(1);
	public static long getNextOrderId() {
		return atomicLong.getAndIncrement();
	}
	public static void resetOrderId() {
		atomicLong = new AtomicLong(1);		
	}

}
