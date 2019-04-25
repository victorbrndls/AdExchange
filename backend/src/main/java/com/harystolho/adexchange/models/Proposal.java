package com.harystolho.adexchange.models;

public class Proposal {

	public enum PaymentMethod {
		PAY_PER_CLICK, PAY_PER_VIEW
	}

	private String id;
	private PaymentMethod paymentMethod;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
