package com.harystolho.adexchange.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class for parameters needed(required) to make a payment transaction
 * 
 * @author Harystolho
 *
 */
public class PaymentProduct {

	public enum PaymentProductType {
		BALANCE_25, BALANCE_50, BALANCE_100
	}

	private final String id;
	private final String amount; // Price
	private final String description;
	private final String weight;
	private final String quantity;

	public PaymentProduct(String id, String amount, String description, String quantity, String weight) {
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.quantity = quantity;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public String getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getWeight() {
		return weight;
	}

	public String getQuantity() {
		return quantity;
	}

	public Map<String, String> getFieldsAsMap() {
		Map<String, String> fields = new HashMap<>();

		fields.put("itemId1", getId());
		fields.put("itemAmount1", getAmount());
		fields.put("itemDescription1", getDescription());
		fields.put("itemQuantity1", getQuantity());
		fields.put("itemWeight1", getWeight());

		return fields;
	}

	public static class PaymentProductBuilder {

		private String id;
		private String amount;
		private String description;
		private String weight;
		private String quantity;

		public PaymentProductBuilder id(String id) {
			this.id = id;
			return this;
		}

		public PaymentProductBuilder amount(String amount) {
			this.amount = amount;
			return this;
		}

		public PaymentProductBuilder description(String description) {
			this.description = description;
			return this;
		}

		public PaymentProductBuilder weight(String weight) {
			this.weight = weight;
			return this;
		}

		public PaymentProductBuilder quantity(String quantity) {
			this.quantity = quantity;
			return this;
		}

		public PaymentProduct build() {
			return new PaymentProduct(id, amount, description, quantity, weight);
		}

	}

}
