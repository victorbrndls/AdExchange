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

	public AdModel buildUsingSpotId(String spotId) {
		ServiceResponse<Spot> response = spotService.getSpot(AEUtils.ADMIN_ACESS_ID, spotId, "contract");

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("getSpot() returned an error [SpotId: {}]", spotId);
			return errorAdModel("INVALID_SPOT_ID");
		}

		Spot spot = response.getReponse();

		return buildUsingSpot(spot);
	}

	private AdModel buildUsingSpot(Spot spot) {
		Contract contract = spot.getContract();

		AdModel model = null;

		if (contract == null) {
			logger.info("Contract is null, returning fallback Ad [SpotId: {}, ContractId: {}]", spot.getId(),
					spot.getContractId());
			model = buildUsingAdId(spot.getFallbackAdId());
		} else {
			// Contract has expired, build an AdModel using the fallback Ad
			if (contract.getExpiration().isBefore(LocalDateTime.now())) {
				model = buildUsingAdId(spot.getFallbackAdId());
			} else { // Contract has not expired, build an AdModel using the contract Ad
				model = buildUsingAdId(contract.getAdId());
			}			
		}

		model.setSpotId(spot.getId());
		return model;
	}

	private AdModel buildUsingAdId(String adId) {
		ServiceResponse<Ad> response = adService.getAdById(adId);

		if (response.getErrorType() != ServiceResponseType.OK) {
			logger.error("Ad is null [id: {}]", adId);
			return errorAdModel();
		}

		return buildUsingAd(response.getReponse());
	}

	private AdModel buildUsingAd(Ad ad) {
		if (ad.getType() == AdType.TEXT) {
			return new AdModel(AdTemplate.assembleUsingTextAd((TextAd) ad));
		} else if (ad.getType() == AdType.IMAGE) {
			return new AdModel(AdTemplate.assembleUsingImageAd((ImageAd) ad));
		}

		logger.error("The Ad type is not valid [id: {}, type: {}]", ad.getId(), ad.getType());
		return errorAdModel("INVALID_AD_TYPE");
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
