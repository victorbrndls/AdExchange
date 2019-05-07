package com.harystolho.adServer.templates;

import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

/**
 * Build the Ad that is displayed in the DOM
 * 
 * @author Harystolho
 *
 */
@Service
public class AdTemplateService {

	private TemplateReader templateReader;

	@Autowired
	public AdTemplateService(TemplateReader templateReader) {
		this.templateReader = templateReader;
	}

	public String assembleUsingTextAd(TextAd ad) {
		// Escape the content to prevent attacks
		String bgColor = Escape.htmlElementContent(ad.getBgColor());
		String textColor = Escape.htmlElementContent(ad.getTextColor());
		String text = Escape.htmlElementContent(ad.getText());

		return String.format(templateReader.getTemplate("TEXT"), bgColor, textColor, text);
	}

	public String assembleUsingImageAd(ImageAd ad) {
		// Escape the content to prevent attacks
		String imageUrl = Escape.htmlElementContent(ad.getImageUrl());

		return String.format(templateReader.getTemplate("IMAGE"), imageUrl);
	}

}
