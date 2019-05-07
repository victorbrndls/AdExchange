package com.harystolho.adServer.templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

@Service
public class AdTemplateService {

	private TemplateReader templateReader;

	@Autowired
	public AdTemplateService(TemplateReader templateReader) {
		this.templateReader = templateReader;
	}

	public String assembleUsingTextAd(TextAd ad) {
		return String.format(templateReader.getTemplate("TEXT"), ad.getBgColor(), ad.getTextColor(), ad.getText());
	}

	public String assembleUsingImageAd(ImageAd ad) {
		return String.format(templateReader.getTemplate("IMAGE"), ad.getImageUrl());
	}

}
