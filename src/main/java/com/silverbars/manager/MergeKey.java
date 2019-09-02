package com.silverbars.manager;

import java.math.BigDecimal;

import com.silverbars.order.Order;
import com.silverbars.order.OrderCurrency;
import com.silverbars.order.OrderType;
import com.silverbars.order.OrderUnit;

/**
 * Different MergeKeys could be provided if required
 * @author ben
 *
 */
public class MergeKey {
	private OrderUnit orderUnit;
	private OrderType orderType;
	private OrderCurrency orderCurrency;
	private BigDecimal orderPrice;
	
	public MergeKey(Order order) {
		this.orderCurrency = order.getOrderCurrency();
		this.orderType = order.getOrderType();
		this.orderUnit = order.getOrderUnit();
		this.orderPrice = order.getOrderPrice();
	}

	OrderUnit getOrderUnit() {
		return orderUnit;
	}

	OrderType getOrderType() {
		return orderType;
	}

	OrderCurrency getOrderCurrency() {
		return orderCurrency;
	}

	BigDecimal getOrderPrice() {
		return orderPrice;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderCurrency == null) ? 0 : orderCurrency.hashCode());
		result = prime * result + ((orderPrice == null) ? 0 : orderPrice.hashCode());
		result = prime * result + ((orderType == null) ? 0 : orderType.hashCode());
		result = prime * result + ((orderUnit == null) ? 0 : orderUnit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MergeKey other = (MergeKey) obj;
		if (orderCurrency != other.orderCurrency)
			return false;
		if (orderPrice == null) {
			if (other.orderPrice != null)
				return false;
		} else if (!orderPrice.equals(other.orderPrice))
			return false;
		if (orderType != other.orderType)
			return false;
		if (orderUnit != other.orderUnit)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MergeKey [orderUnit=" + orderUnit + ", orderType=" + orderType + ", orderCurrency=" + orderCurrency
				+ ", orderPrice=" + orderPrice + "]";
	}
	
	

}
