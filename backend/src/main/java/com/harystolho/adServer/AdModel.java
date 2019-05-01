package com.harystolho.adServer;

/**
 * Represents an Ad that is rendered in the DOM, it contains raw HTML that is
 * built using an Ad and a template
 * 
 * @author Harystolho
 *
 */
public class AdModel {

	private String content;

	public AdModel(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

}
