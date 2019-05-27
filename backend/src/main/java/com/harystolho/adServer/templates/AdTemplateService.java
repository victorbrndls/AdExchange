package com.harystolho.adserver.templates;

import org.apache.tomcat.util.security.Escape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.parser.ad.AdContentParser;
import com.harystolho.adexchange.parser.ad.TagNodeWriter;

/**
 * Build the Ad that is displayed in the DOM
 * 
 * @author Harystolho
 *
 */
@Service
public class AdTemplateService {

	private TemplateReader templateReader;
	private TagNodeWriter tagNodeWriter;

	@Autowired
	public AdTemplateService(TemplateReader templateReader, TagNodeWriter tagNodeWriter) {
		this.templateReader = templateReader;
		this.tagNodeWriter = tagNodeWriter;
	}

	public String assembleUsingTextAd(TextAd ad) {
		// Escape the content to prevent attacks
		String bgColor = Escape.htmlElementContent(ad.getBgColor());
		String textColor = Escape.htmlElementContent(ad.getTextColor());
		String text = getTextAdParsedOutputAsHTML(ad);

		return String.format(templateReader.getTemplate("TEXT"), bgColor, textColor, text);
	}

	public String assembleUsingImageAd(ImageAd ad) {
		// Escape the content to prevent attacks
		String imageUrl = Escape.htmlElementContent(ad.getImageUrl());

		return String.format(templateReader.getTemplate("IMAGE"), imageUrl);
	}

	private String getTextAdParsedOutputAsHTML(TextAd ad) {
		AdContentParser parser = new AdContentParser();
		parser.setInput(ad.getText());

		return tagNodeWriter.writeHTML(parser.parse());
	}
}
