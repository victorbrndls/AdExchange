package com.harystolho.adServer;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;

/**
 * Builds an {@link AdModel} from a {@link Spot} and a template
 * 
 * @author Harystolho
 *
 */
@Service
public class AdModelBuilder {

	private static final Logger logger = LogManager.getLogger();

	private SpotService spotService;
	private AdService adService;

	@Autowired
	private AdModelBuilder(SpotService spotService, AdService adService) {
		this.spotService = spotService;
		this.adService = adService;
	}

	public AdModel build(String spotId) {
		ServiceResponse<Spot> response = spotService.getSpot(AEUtils.ADMIN_ACESS_ID, spotId, "contract");

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("getSpot() returned an error [SpotId: {}]", spotId);
			return errorAdModel();
		}

		Spot spot = response.getReponse();
		Contract contract = spot.getContract();

		if (contract == null) {
			logger.error("Contract is null [SpotId: {}]", spotId);
			return errorAdModel();
		}

		return build(spot, contract);
	}

	private AdModel build(Spot spot, Contract contract) {
		// Contract has expired, build an AdModel using the fallback Ad
		if (contract.getExpiration().isBefore(LocalDateTime.now())) {
			return buildUsingAdId(spot.getFallbackAdId());
		} else { // Contract has not expired, build an AdModel using the contract Ad
			return buildUsingAdId(contract.getAdId());
		}
	}

	private AdModel buildUsingAdId(String adId) {
		ServiceResponse<Ad> response = adService.getAdById(adId);

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("Ad is null [AdId: {}]", adId);
			return errorAdModel();
		}

		Ad ad = response.getReponse();

		return buildUsingAd(ad);
	}

	private AdModel buildUsingAd(Ad ad) {
		if (ad.getType() == AdType.TEXT) {
			return buildUsingTextAd((TextAd) ad);
		} else if (ad.getType() == AdType.IMAGE) {
			return buildUsingImageAd((ImageAd) ad);
		}

		logger.error("The Ad type is not valid [adId: {}]", ad.getId());
		return errorAdModel("INVALID_AD_TYPE");
	}

	private AdModel buildUsingTextAd(TextAd ad) {
		String content = AdTemplate.assembleUsingTextAd(ad);
		return new AdModel(content);
	}

	private AdModel buildUsingImageAd(ImageAd ad) {
		String content = AdTemplate.assembleUsingImageAd(ad);
		return new AdModel(content);
	}

	private AdModel errorAdModel() {
		return errorAdModel("error");
	}

	private AdModel errorAdModel(String error) {
		AdModel model = new AdModel("");
		model.setError(error);

		return model;
	}
}
