package com.silverbars.order;

import java.math.BigDecimal;

/**
 * Order also needs an OrderId (so that the correct Order can be cancelled)
 * @author ben
 *
 */
public class Order {
	private long orderId;
	private String userId;
	private BigDecimal quantity;
	private OrderUnit orderUnit;
	private OrderType orderType;
	private OrderCurrency orderCurrency;
	private BigDecimal orderPrice;

	public static class Builder {
		private long orderId;
		private String userId;
		private BigDecimal quantity;
		private OrderUnit orderUnit;
		private OrderType orderType;
		private OrderCurrency orderCurrency;
		private BigDecimal orderPrice;

		public Builder() {
			this.orderId = OrderUtil.getNextOrderId();
		}

		public Order build() {
			Order order =  new Order();
			if(this.userId==null || "".equals(this.userId.trim())) {
				throw new IllegalArgumentException("missing userId");
			}
			if(this.quantity==null || this.quantity.compareTo(new BigDecimal("0"))==0) {
				throw new IllegalArgumentException("missing quantity");
			}
			if(this.orderUnit==null) {
				throw new IllegalArgumentException("missing orderUnit");
			}
			if(this.orderType==null) {
				throw new IllegalArgumentException("missing orderType");
			}
			if(this.orderCurrency==null) {
				throw new IllegalArgumentException("missing orderCurrency");
			}
			if(this.orderPrice==null || this.orderPrice.compareTo(new BigDecimal("0"))==0) {
				throw new IllegalArgumentException("missing orderPrice");
			}

			order.orderId = this.orderId;
			order.userId = this.userId;
			order.quantity = this.quantity;
			order.orderUnit = this.orderUnit;
			order.orderType = this.orderType;
			order.orderCurrency = this.orderCurrency;
			order.orderPrice = this.orderPrice;

			return order;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder quantity(BigDecimal quantity) {
			this.quantity = quantity;
			return this;
		}

		public Builder orderUnit(OrderUnit orderUnit) {
			this.orderUnit = orderUnit;
			return this;
		}

		public Builder orderType(OrderType orderType) {
			this.orderType = orderType;
			return this;
		}

		public Builder orderCurrency(OrderCurrency orderCurrency) {
			this.orderCurrency = orderCurrency;
			return this;
		}

		public Builder orderPrice(BigDecimal orderPrice) {
			this.orderPrice = orderPrice;
			return this;
		}
	}

	private Order() {
	}

	public long getOrderId() {
		return orderId;
	}

	public String getUserId() {
		return userId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public OrderUnit getOrderUnit() {
		return orderUnit;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public OrderCurrency getOrderCurrency() {
		return orderCurrency;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
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
		Order other = (Order) obj;
		if (orderId != other.orderId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", userId=" + userId + ", quantity=" + quantity + ", orderUnit="
				+ orderUnit + ", orderType=" + orderType + ", orderCurrency=" + orderCurrency + ", orderPrice="
				+ orderPrice + "]";
	}


}
