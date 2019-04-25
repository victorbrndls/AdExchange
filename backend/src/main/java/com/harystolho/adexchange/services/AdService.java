package com.harystolho.adexchange.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.AdRepository;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AdService {

	private static final Logger logger = LogManager.getLogger();

	private AdRepository adRepository;

	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

	public Pair<ServiceResponse, List<Ad>> getUserAds() {
		return Pair.of(ServiceResponse.OK, adRepository.getAdsByAccountId());
	}

	public Pair<ServiceResponse, Ad> getAdById(String id) {
		return Pair.of(ServiceResponse.OK, adRepository.getAdById(id));
	}

	public Pair<ServiceResponse, Ad> createAd(String name, String type, String refUrl, String text, String bgColor,
			String textColor, String imageUrl) {
		if (!type.equals("TEXT") && !type.equals("IMAGE")) {
			logger.error("The ad type must be either 'TEXT' or 'IMAGE' (value: {})", type);
			return Pair.of(ServiceResponse.FAIL, null);
		}

		Ad ad = null;

		if (type.equals("TEXT")) {
			ad = createTextAd(name, text, bgColor, textColor, refUrl);
		} else if (type.equals("IMAGE")) {
			ad = createImageAd(name, imageUrl, refUrl);
		}

		if (ad == null)
			return Pair.of(ServiceResponse.FAIL, null);

		return Pair.of(ServiceResponse.OK, ad);
	}

	private Ad createTextAd(String name, String text, String bgColor, String textColor, String refUrl) {
		TextAd ad = new TextAd();

		ad.setName(name);
		ad.setText(text);
		ad.setBgColor(bgColor);
		ad.setTextColor(textColor);
		ad.setRefUrl(refUrl);

		return adRepository.save(ad);
	}

	private Ad createImageAd(String name, String imageUrl, String refUrl) {
		ImageAd ad = new ImageAd();

		ad.setName(name);
		ad.setImageUrl(imageUrl);
		ad.setRefUrl(refUrl);

		return adRepository.save(ad);
	}

	public String getAccountIdUsingAdId(String id) {
		Ad ad = adRepository.getAdById(id);
		return ad.getAccountId();
	}

}
