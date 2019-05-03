package com.harystolho.adexchange.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.repositories.ad.AdRepository;
import com.harystolho.adexchange.utils.Nothing;

@Service
public class AdService {

	private AdRepository adRepository;

	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

	public ServiceResponse<List<Ad>> getAdsByAccountId(String accountId) {
		return ServiceResponse.ok(adRepository.getAdsByAccountId(accountId));
	}

	public ServiceResponse<Ad> getAdById(String id) {
		return ServiceResponse.ok(adRepository.getAdById(id));
	}

	public ServiceResponse<List<Ad>> getAdsById(String ids) {
		return ServiceResponse.ok(adRepository.getAdsById(Arrays.asList(ids.split(","))));
	}

	public ServiceResponse<Ad> createAd(String accountId, String name, String type, String refUrl, String text,
			String bgColor, String textColor, String imageUrl) {
		if (!verifyAdType(type))
			return ServiceResponse.fail("The type must TEXT or IMAGE");

		Ad ad = null;

		if (type.equals("TEXT")) {
			ad = createTextAd(name, text, bgColor, textColor, refUrl);
		} else if (type.equals("IMAGE")) {
			ad = createImageAd(name, imageUrl, refUrl);
		}

		if (ad == null)
			return ServiceResponse.fail("Can't create Ad");

		ad.setAccountId(accountId);

		Ad saved = adRepository.save(ad);

		return ServiceResponse.ok(saved);
	}

	public ServiceResponse<Ad> updateAd(String accountId, String id, String name, String type, String refUrl,
			String text, String bgColor, String textColor, String imageUrl) {
		if (!verifyAdType(type))
			return ServiceResponse.fail("The type must TEXT or IMAGE");

		Ad ad = adRepository.getAdById(id);

		if (ad == null)
			return ServiceResponse.fail("Ad id is not valid");

		if (!ad.getAccountId().equals(accountId))
			return ServiceResponse.unauthorized();

		ad.setName(name);
		ad.setRefUrl(refUrl);
		ad.setType(AdType.valueOf(type));

		if (type.equals("TEXT")) {
			TextAd tAd = (TextAd) ad;
			tAd.setText(text);
			tAd.setBgColor(bgColor);
			tAd.setTextColor(textColor);
			ad = tAd;
		} else if (type.equals("IMAGE")) {
			ImageAd iAd = (ImageAd) ad;
			iAd.setImageUrl(imageUrl);
			ad = iAd;
		}

		Ad saved = adRepository.save(ad);
		return ServiceResponse.ok(saved);
	}

	private boolean verifyAdType(String type) {
		return type.equals("TEXT") || type.equals("IMAGE");
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

	public String duplicateAd(String adId) {
		Ad ad = adRepository.getAdById(adId);

		if (ad != null) {
			ad.setId(null);
			ad.setAccountId("ADMIN");

			Ad newAd = adRepository.save(ad);
			return newAd.getId();
		}

		return null;
	}

	public String getAccountIdUsingAdId(String id) {
		Ad ad = adRepository.getAdById(id);
		return ad.getAccountId();
	}

	// TODO also delete the proposals that have this ad in them
	public ServiceResponse<Nothing> deleteAdById(String accountId, String id) {
		Ad ad = adRepository.getAdById(id);

		if (ad != null && ad.getAccountId().equals(accountId)) {
			adRepository.deleteById(id);
			return ServiceResponse.ok(null);
		}

		return ServiceResponse.fail("The Ad doesn't belong to this account");
	}

}
