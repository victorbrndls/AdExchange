package com.harystolho.adexchange.controllers.models;

public class AdBuilderModel {

	public AdBuilderModel() {
		// Default values

		this.type = "TEXT";
		this.refUrl = "https://localhost:99999";
		this.text = "Missing text";
		this.textAlignment = "LEFT";
		this.textSize = 16;
		this.bgColor = "#000";
		this.textColor = "#fff";
	}

	private String accountId;
	private String id;

	private String name;
	private String type;
	private String refUrl;

	private String text;
	private String textAlignment;
	private int textSize;
	private String bgColor;
	private String textColor;

	private String imageUrl;

	public AdBuilderModel setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public AdBuilderModel setId(String id) {
		this.id = id;
		return this;
	}

	public AdBuilderModel setName(String name) {
		this.name = name;
		return this;
	}

	public AdBuilderModel setType(String type) {
		this.type = type;
		return this;
	}

	public AdBuilderModel setRefUrl(String refUrl) {
		this.refUrl = refUrl;
		return this;
	}

	public AdBuilderModel setText(String text) {
		this.text = text;
		return this;
	}

	public AdBuilderModel setTextAlignment(String textAlignment) {
		this.textAlignment = textAlignment;
		return this;
	}

	public AdBuilderModel setTextSize(int textSize) {
		this.textSize = textSize;
		return this;
	}

	public AdBuilderModel setBgColor(String bgColor) {
		this.bgColor = bgColor;
		return this;
	}

	public AdBuilderModel setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}

	public AdBuilderModel setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}

	// Getters //

	public String getAccountId() {
		return accountId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getRefUrl() {
		return refUrl;
	}

	public String getText() {
		return text;
	}

	public String getTextAlignment() {
		return textAlignment;
	}

	public int getTextSize() {
		return textSize;
	}

	public String getBgColor() {
		return bgColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public String getImageUrl() {
		return imageUrl;
	}

}
