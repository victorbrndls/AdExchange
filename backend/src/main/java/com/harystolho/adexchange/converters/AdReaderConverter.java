package com.harystolho.adexchange.converters;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;

@ReadingConverter
public class AdReaderConverter implements Converter<Document, Ad> {

	@Override
	public Ad convert(Document source) {
		Ad ad = null;

		switch (source.getString("type")) {
		case "TEXT":
			TextAd textAd = new TextAd();
			textAd.setText(source.getString("text"));
			textAd.setTextColor(source.getString("textColor"));
			textAd.setBgColor(source.getString("bgColor"));
			ad = textAd;
			break;
		case "IMAGE":
			ImageAd imageAd = new ImageAd();
			imageAd.setImageUrl(source.getString("imageUrl"));
			ad = imageAd;
			break;
		default:
			return null;
		}

		ad.setId(source.getObjectId("_id").toString());
		ad.setAccountId(source.getString("accountId"));
		ad.setName(source.getString("name"));
		ad.setRefUrl(source.getString("refUrl"));

		return ad;
	}

}
