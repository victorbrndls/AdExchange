package com.harystolho.adServer;

import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

public class AdTemplate {

	public static String assembleUsingTextAd(TextAd ad) {
		return String.format("<div>%s</div>", ad.getText());
	}

	public static String assembleUsingImageAd(ImageAd ad) {
		return String.format("<div><img src=\"%s\"></div>", ad.getImageUrl());
	}

}
