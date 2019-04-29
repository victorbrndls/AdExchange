package com.harystolho.adexchange.models;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.harystolho.adexchange.models.Contract.PaymentMethod;

@Document("proposals")
public class Proposal {

	public Proposal() {
		creationDate = Date.from(Instant.now());
		version = 1;
		rejected = false;
	}

	private String id;
	private String creatorAccountId;
	private String websiteId;
	private String adId;
	private int duration;
	private PaymentMethod paymentMethod;
	private String paymentValue;
	private Date creationDate;
	private int version;
	private boolean rejected;

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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPaymentValue() {
		return paymentValue;
	}

	public void setPaymentValue(String paymentValue) {
		this.paymentValue = paymentValue;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCreatorAccountId() {
		return creatorAccountId;
	}

	public void setCreatorAccountId(String creatorAccountId) {
		this.creatorAccountId = creatorAccountId;
	}
}
