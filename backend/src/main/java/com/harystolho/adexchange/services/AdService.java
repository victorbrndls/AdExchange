package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.repositories.ad.AdRepository;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AdService {

	private AdRepository adRepository;

	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

	public Pair<ServiceResponse, List<Ad>> getAdsByAccountId(String accountId) {
		return Pair.of(ServiceResponse.OK, adRepository.getAdsByAccountId(accountId));
	}

	public Pair<ServiceResponse, Ad> getAdById(String id) {
		return Pair.of(ServiceResponse.OK, adRepository.getAdById(id));
	}

	public Pair<ServiceResponse, Ad> createAd(String accountId, String name, String type, String refUrl, String text,
			String bgColor, String textColor, String imageUrl) {
		if (!type.equals("TEXT") && !type.equals("IMAGE")) {
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

		ad.setAccountId(accountId);
		adRepository.save(ad);

		return Pair.of(ServiceResponse.OK, ad);
	}

	private Ad createTextAd(String name, String text, String bgColor, String textColor, String refUrl) {
		TextAd ad = new TextAd();

		ad.setName(name);
		ad.setText(text);
		ad.setBgColor(bgColor);
		ad.setTextColor(textColor);
		ad.setRefUrl(refUrl);

		return ad;
	}

	private Ad createImageAd(String name, String imageUrl, String refUrl) {
		ImageAd ad = new ImageAd();

		ad.setName(name);
		ad.setImageUrl(imageUrl);
		ad.setRefUrl(refUrl);

		return ad;
	}

	public String getAccountIdUsingAdId(String id) {
		Ad ad = adRepository.getAdById(id);
		return ad.getAccountId();
	}

}
