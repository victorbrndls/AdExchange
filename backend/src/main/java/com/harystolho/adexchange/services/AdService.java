package com.harystolho.adexchange.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.parser.ad.AdContentParser;
import com.harystolho.adexchange.parser.ad.TagNode;
import com.harystolho.adexchange.repositories.ad.AdRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Nothing;

@Service
public class AdService {

	private static final String HEX_COLOR_REGEX = "^(#[0-9a-z]{1,6})";
	private static final String URL_REGEX = "^(https{0,1}:\\/\\/)\\S+";

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

	public ServiceResponse<Ad> createOrUpdateAd(String accountId, String id, String name, String type, String refUrl,
			String text, String bgColor, String textColor, String imageUrl) {

		ServiceResponseType error = verifyAdFields(name, type, refUrl, text, bgColor, textColor, imageUrl);
		if (error != ServiceResponseType.OK)
			return ServiceResponse.error(error);

		boolean updateExistingAd = false;

		if (id != null) { // Update existing ad
			Ad adToUpdate = adRepository.getAdById(id);
			if (adToUpdate != null) {
				if (!adToUpdate.isAuthorized(accountId)) // Check if user has authorization to update the ad
					return ServiceResponse.unauthorized();

				updateExistingAd = true;
			} else {
				return ServiceResponse.error(ServiceResponseType.INVALID_AD_ID);
			}
		}

		Ad ad = null;

		if (type.equals("TEXT")) {
			TextAd tAd = new TextAd();
			tAd.setText(text);
			tAd.setBgColor(bgColor);
			tAd.setTextColor(textColor);
			ad = tAd;
		} else if (type.equals("IMAGE")) {
			ImageAd iAd = new ImageAd();
			iAd.setImageUrl(imageUrl);
			ad = iAd;
		}

		ad.setName(name);
		ad.setRefUrl(refUrl);
		ad.setAccountId(accountId);

		if (updateExistingAd)
			ad.setId(id);

		return ServiceResponse.ok(adRepository.save(ad));
	}

	public String getAccountIdUsingAdId(String id) {
		Ad ad = adRepository.getAdById(id);
		return ad.getAccountId();
	}

	// TODO also delete the proposals that have this ad in them
	public ServiceResponse<Nothing> deleteAdById(String accountId, String id) {
		Ad ad = adRepository.getAdById(id);

		if (ad != null && ad.getAccountId().equals(accountId)) {
			adRepository.removeById(id);
			return ServiceResponse.ok(null);
		}

		return ServiceResponse.fail("The Ad doesn't belong to this account");
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

	private ServiceResponseType verifyAdFields(String name, String type, String refUrl, String text, String bgColor,
			String textColor, String imageUrl) {

		if (StringUtils.isEmpty(name))
			return ServiceResponseType.INVALID_AD_NAME;

		if (!refUrl.matches(URL_REGEX))
			return ServiceResponseType.INVALID_AD_REF_URL;

		if (type.equals("TEXT")) {
			if (StringUtils.isEmpty(text))
				return ServiceResponseType.INVALID_AD_TEXT;

			if (!bgColor.matches(HEX_COLOR_REGEX))
				return ServiceResponseType.INVALID_AD_BG_COLOR;

			if (!textColor.matches(HEX_COLOR_REGEX))
				return ServiceResponseType.INVALID_AD_TEXT_COLOR;

			return ServiceResponseType.OK;
		} else if (type.equals("IMAGE")) {
			if (!imageUrl.matches(URL_REGEX))
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
