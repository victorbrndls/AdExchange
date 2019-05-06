package com.harystolho.adexchange.models;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.Nullable;

import com.harystolho.adexchange.models.Contract.PaymentMethod;

@Document("proposals")
public class Proposal {

	public Proposal() {
		creationDate = Date.from(Instant.now());
		version = 1;
		rejected = false;
		inProposerSent = true;
	}

	private String id;

	@Field("accountId.proposer")
	private String proposerId;
	@Field("accountId.proposee")
	private String proposeeId;

	private String websiteId;
	private String adId;
	private int duration;
	private PaymentMethod paymentMethod;
	private String paymentValue;
	private Date creationDate;
	private int version;

	// True if the proposal is in the 'Sent' container for the proposer
	private boolean inProposerSent;
	private boolean rejected;

	@Transient
	private Website website;
	
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

	public String getProposerId() {
		return proposerId;
	}

	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}

	public String getProposeeId() {
		return proposeeId;
	}

	public void setProposeeId(String proposeeId) {
		this.proposeeId = proposeeId;
	}

	public boolean isInProposerSent() {
		return inProposerSent;
	}

	public void setInProposerSent(boolean inProposerSent) {
		this.inProposerSent = inProposerSent;
	}

	@Nullable
	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

}
