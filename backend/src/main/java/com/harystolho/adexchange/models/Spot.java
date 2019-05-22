package com.harystolho.adexchange.models;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.utils.AEUtils;

@Document("spots")
public class Spot {

	private String id;
	private String accountId;
	private String name;
	private String contractId;
	private String fallbackAdId;

	@Transient
	private Contract contract;
	@Transient
	private Ad fallbackAd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getFallbackAdId() {
		return fallbackAdId;
	}

	public void setFallbackAdId(String adId) {
		this.fallbackAdId = adId;
	}

	/**
	 * @param accessId
	 * @return TRUE if the {accessId} has authority to edit this object
	 */
	public boolean isAuthorized(String accessId) {
		return accessId.equals(getAccountId()) || accessId.equals(AEUtils.ADMIN_ACESS_ID);
	}

	@Nullable
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Nullable
	public Ad getFallbackAd() {
		return fallbackAd;
	}

	public void setFallbackAd(Ad fallbackAd) {
		this.fallbackAd = fallbackAd;
	}

}
