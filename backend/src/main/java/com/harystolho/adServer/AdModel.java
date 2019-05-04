package com.harystolho.adServer;

/**
 * Represents an Ad that is rendered in the DOM, it contains raw HTML that is
 * built using an Ad and a template
 * 
 * @author Harystolho
 *
 */
public class AdModel {

	private String spotId;
	private String content;
	private String error;
	private String refUrl;

	public AdModel(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSpotId() {
		return spotId;
	}

	public void setSpotId(String spotId) {
		this.spotId = spotId;
	}

	public String getRedirectUrl() {
		return refUrl;
	}

	public void setRedirectUrl(String url) {
		this.refUrl = url;
	}

}
