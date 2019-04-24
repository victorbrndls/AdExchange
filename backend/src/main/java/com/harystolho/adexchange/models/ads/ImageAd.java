package com.harystolho.adexchange.models.ads;

public class ImageAd extends Ad {

	private String imageUrl;

	public ImageAd() {
		super(AdType.IMAGE);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
