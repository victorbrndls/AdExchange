package com.harystolho.adServer;

import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

public class AdTemplate {

	private static final String TEXT_AD_STYLE = "width: 100%%; height: 100%%; padding: 6px 9px; overflow-y: hidden;";

	private static final String TEXT_AD_TEMPLATE = "<a native href=\"%s\" target=\"_blank\" style=\"text-decoration: none;\">"
			+ "<div style=\"background-color: %s; color: %s;" + TEXT_AD_STYLE + "\"> %s </div></a>";
	private static final String IMAGE_AD_TEMPLATE = "<a native href=\"%s\" target=\"_blank\">"
			+ "<div style=\"width: 100%%; height: 100%%;\"> <img src=\"%s\" style=\"height: 100%%; width: 100%%;\"> </div></a>";

	public static String assembleUsingTextAd(TextAd ad) {
		return String.format(TEXT_AD_TEMPLATE, ad.getRefUrl(), ad.getBgColor(), ad.getTextColor(), ad.getText());
	}

	public static String assembleUsingImageAd(ImageAd ad) {
		return String.format(IMAGE_AD_TEMPLATE, ad.getRefUrl(), ad.getImageUrl());
	}

}
