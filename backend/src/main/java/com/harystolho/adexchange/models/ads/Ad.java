package com.harystolho.adexchange.models.ads;

public class Ad {

	public static enum AdType {
		TEXT, IMAGE
	}

	private String id;
	private String accountId;
	private String name;
	private String refUrl;
	private AdType type;

	public Ad(AdType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AdType getType() {
		return type;
	}

	public void setType(AdType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRefUrl() {
		return refUrl;
	}

	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
