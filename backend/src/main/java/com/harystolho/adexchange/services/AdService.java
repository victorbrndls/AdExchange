package com.harystolho.adexchange.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.controllers.models.AdBuilderModel;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.models.ads.TextAd.TextAlignment;
import com.harystolho.adexchange.parser.ad.AdContentParser;
import com.harystolho.adexchange.parser.ad.TagNode;
import com.harystolho.adexchange.repositories.ad.AdRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;

@Service
public class AdService {

	private static final String HEX_COLOR_REGEX = "^(#[0-9a-z]{1,6})";

	private AdRepository adRepository;

	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

	public ServiceResponse<List<Ad>> getAdsByAccountId(String accountId) {
		return getAdsByAccountId(accountId, "");
	}

	/**
	 * 
	 * @param accountId
	 * @param embed     ["parsedOutput"]
	 * @return
	 */
	public ServiceResponse<List<Ad>> getAdsByAccountId(String accountId, String embed) {
		List<Ad> ads = adRepository.getAdsByAccountId(accountId);

		if (embed.contains("parsedOutput")) {
			ads.forEach((ad) -> {
				if (ad instanceof TextAd) {
					embedParsedOutput((TextAd) ad);
				}
			});
		}

		return ServiceResponse.ok(ads);
	}

	/**
	 * 
	 * @param id
	 * @param embed ["parsedOutput"]
	 * @return
	 */
	public ServiceResponse<Ad> getAdById(String id, String embed) {
		Ad ad = adRepository.getAdById(id);

		if (embed.contains("parsedOutput")) {
			if (ad instanceof TextAd) {
				ad = embedParsedOutput((TextAd) ad);
			}
		}

		return ServiceResponse.ok(ad);
	}

	public ServiceResponse<Ad> getAdById(String id) {
		return getAdById(id, "");
	}

	public ServiceResponse<List<Ad>> getAdsById(String ids) {
		return ServiceResponse.ok(adRepository.getAdsById(Arrays.asList(ids.split(","))));
	}

	public ServiceResponse<Ad> createOrUpdateAd(AdBuilderModel model) {

		ServiceResponseType error = verifyAdFields(model);
		if (error != ServiceResponseType.OK)
			return ServiceResponse.error(error);

		boolean updateExistingAd = false;

		if (model.getId() != null) { // Update existing ad
			Ad adToUpdate = adRepository.getAdById(model.getId());
			if (adToUpdate != null) {
				if (!adToUpdate.isAuthorized(model.getAccountId())) // Check if user has authorization to update the ad
					return ServiceResponse.unauthorized();

				updateExistingAd = true;
			} else {
				return ServiceResponse.error(ServiceResponseType.INVALID_AD_ID);
			}
		}

		Ad ad = null;

		if (model.getType().equals("TEXT")) {
			TextAd tAd = new TextAd();
			tAd.setText(model.getText());
			tAd.setTextAlignment(TextAlignment.valueOf(model.getTextAlignment()));
			tAd.setTextSize(model.getTextSize());
			tAd.setBgColor(model.getBgColor());
			tAd.setTextColor(model.getTextColor());
			ad = tAd;
		} else if (model.getType().equals("IMAGE")) {
			ImageAd iAd = new ImageAd();
			iAd.setImageUrl(model.getImageUrl());
			ad = iAd;
		}

		ad.setName(model.getName());
		ad.setRefUrl(model.getRefUrl());
		ad.setAccountId(model.getAccountId());

		if (updateExistingAd)
			// Set the id so the db replaces the old version instead of creating a new one
			ad.setId(model.getId());

		return ServiceResponse.ok(adRepository.save(ad));
	}

	public String getAccountIdUsingAdId(String id) {
		Ad ad = adRepository.getAdById(id);
		return ad.getAccountId();
	}

	// TODO also delete the proposals that have this ad in them
	public ServiceResponseType deleteAdById(String accountId, String id) {
		Ad ad = adRepository.getAdById(id);

		if (ad != null && ad.getAccountId().equals(accountId)) {
			adRepository.removeById(id);
			return ServiceResponseType.OK;
		}

		return ServiceResponseType.FAIL;
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

	public void removeAd(String id) {
		adRepository.removeById(id);
	}

	private ServiceResponseType verifyAdFields(AdBuilderModel model) {

		if (!StringUtils.hasText(model.getName()))
			return ServiceResponseType.INVALID_AD_NAME;

		if (!model.getRefUrl().matches(AEUtils.URL_REGEX))
			return ServiceResponseType.INVALID_AD_REF_URL;

		if (model.getType().equals("TEXT")) {
			if (!StringUtils.hasText(model.getText()))
				return ServiceResponseType.INVALID_AD_TEXT;

			try {
				TextAlignment.valueOf(model.getTextAlignment());
			} catch (Exception e) {
				return ServiceResponseType.INVALID_AD_TEXT_ALIGNMENT;
			}

			if (model.getTextSize() < 1)
				return ServiceResponseType.INVALID_AD_TEXT_SIZE;
			
			if (!model.getBgColor().matches(HEX_COLOR_REGEX))
				return ServiceResponseType.INVALID_AD_BG_COLOR;

			if (!model.getTextColor().matches(HEX_COLOR_REGEX))
				return ServiceResponseType.INVALID_AD_TEXT_COLOR;

			return ServiceResponseType.OK;
		} else if (model.getType().equals("IMAGE")) {
			if (!model.getImageUrl().matches(AEUtils.URL_REGEX))
				return ServiceResponseType.INVALID_AD_IMAGE_URL;

			return ServiceResponseType.OK;
		} else {
			return ServiceResponseType.INVALID_AD_TYPE;
		}
	}

	public ServiceResponse<List<TagNode>> parseInput(String input) {
		AdContentParser parser = new AdContentParser();
		parser.setInput(input);

		return ServiceResponse.ok(parser.parse());
	}

	private TextAd embedParsedOutput(TextAd ad) {
		AdContentParser parser = new AdContentParser();
		parser.setInput(ad.getText());
		ad.setParsedOutput(parser.parse());

		return ad;
	}

}
