package com.harystolho.adexchange.models.ads;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.harystolho.adexchange.parser.ad.TagNode;

public class TextAd extends Ad {

	private String text;
	private String bgColor;
	private String textColor;

	@Transient
	private List<TagNode> parsedOutput;

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

	public List<TagNode> getParsedOutput() {
		return parsedOutput;
	}

	public void setParsedOutput(List<TagNode> parsedOutput) {
		this.parsedOutput = parsedOutput;
	}

}
