package com.harystolho.adexchange.models.ads;

public class TextAd extends Ad {

	private String text;
	private String bgColor;
	private String textColor;

	public TextAd() {
		super(AdType.TEXT);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

}
