package com.harystolho.adexchange.controllers.models;

public class WebsiteBuilderModel implements Cloneable {

	private String accountId;
	private String id;
	private String name;
	private String url;
	private String monthlyImpressions;
	private String logoUrl;
	private String description;
	private String categories;

	public WebsiteBuilderModel setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public WebsiteBuilderModel setId(String id) {
		this.id = id;
		return this;
	}

	public WebsiteBuilderModel setName(String name) {
		this.name = name;
		return this;
	}

	public WebsiteBuilderModel setUrl(String url) {
		this.url = url;
		return this;
	}

	public WebsiteBuilderModel setLogoUrl(String url) {
		this.logoUrl = url;
		return this;
	}

	public WebsiteBuilderModel setDescription(String description) {
		this.description = description;
		return this;
	}

	public WebsiteBuilderModel setCategories(String categories) {
		this.categories = categories;
		return this;
	}

	public WebsiteBuilderModel setMonthlyImpressions(String impressions) {
		this.monthlyImpressions = impressions;
		return this;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getMonthlyImpressions() {
		return monthlyImpressions;
	}

	public String getLogoURL() {
		return logoUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getCategories() {
		return categories;
	}

	public WebsiteBuilderModel clone() {
		WebsiteBuilderModel clone = new WebsiteBuilderModel();

		clone.setAccountId(accountId);
		clone.setId(id);
		clone.setName(name);
		clone.setUrl(url);
		clone.setLogoUrl(logoUrl);
		clone.setMonthlyImpressions(monthlyImpressions);
		clone.setDescription(description);
		clone.setCategories(categories);

		return clone;
	}

}
