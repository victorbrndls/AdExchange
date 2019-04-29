package com.harystolho.adexchange.models;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("contracts")
public class Contract {

	public enum PaymentMethod {
		PAY_PER_CLICK, PAY_PER_VIEW
	}

	private String id;
	private String creatorId; // The Ad owner id
	private String acceptorId; // The Website owner id
	private LocalDateTime expiration;
	private String websiteId;
	private String adId;
	private PaymentMethod paymentMethod;
	private String paymentValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}

	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(String paymentValue) {
		this.paymentValue = paymentValue;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(String acceptorId) {
		this.acceptorId = acceptorId;
	}

}
