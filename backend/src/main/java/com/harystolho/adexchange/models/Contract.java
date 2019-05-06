package com.harystolho.adexchange.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import com.harystolho.adexchange.models.ads.Ad;

@Document("contracts")
public class Contract {

	public enum PaymentMethod {
		PAY_PER_CLICK, PAY_PER_VIEW
	}

	private String id;
	private String creatorId; // The Ad owner id
	private String creatorContractName;
	private String acceptorId; // The Website owner id
	private String acceptorContractName;

	private LocalDateTime expiration;
	private String websiteId;
	private String adId;
	private PaymentMethod paymentMethod;
	private String paymentValue;

	@Transient
	private Website website;
	@Transient
	private Ad ad;

	public Contract() {
		super();
	}

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

	public boolean hasExpired() {
		return expiration.isBefore(LocalDateTime.now());
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

	public boolean isAuthorized(String accessId) {
		return accessId.equals(acceptorId) || accessId.equals(creatorId);
	}

	@Nullable
	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	@Nullable
	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public String getCreatorContractName() {
		return creatorContractName;
	}

	public void setCreatorContractName(String creatorContractName) {
		this.creatorContractName = creatorContractName;
	}

	public String getAcceptorContractName() {
		return acceptorContractName;
	}

	public void setAcceptorContractName(String acceptorContractName) {
		this.acceptorContractName = acceptorContractName;
	}

}
