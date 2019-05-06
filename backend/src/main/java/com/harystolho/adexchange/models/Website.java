package com.harystolho.adexchange.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("websites")
public class Website {

	public Website(String accountId, String url) {
		this.accountId = accountId;
		this.url = url;
	}

	private String id;
	private String accountId;

	private String name;
	private String url;
	private String logoUrl;
	private String description;
	private String[] categories;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public boolean isAuthorized(String accessId) {
		return accessId.equals(accountId);
	}
}
