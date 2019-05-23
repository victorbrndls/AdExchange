package com.harystolho.adServer.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adServer.AdModel;
import com.harystolho.adServer.controllers.UrlRedirectorController;
import com.harystolho.adServer.templates.AdTemplateService;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.Ad.AdType;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;

/**
 * Builds an {@link AdModel}
 * 
 * @author Harystolho
 *
 */
@Service
public class AdModelService {

	private static final Logger logger = LogManager.getLogger();

	private SpotService spotService;
	private AdService adService;
	private UrlRedirecterService urlRedirecterService;
	private AdTemplateService adTemplateService;
	private AccountService accountService;

	@Autowired
	private AdModelService(SpotService spotService, AdService adService, UrlRedirecterService urlRedirecterService,
			AdTemplateService adTemplateService, AccountService accountService) {
		this.spotService = spotService;
		this.adService = adService;
		this.urlRedirecterService = urlRedirecterService;
		this.adTemplateService = adTemplateService;
		this.accountService = accountService;
	}

	public AdModel buildUsingSpotId(String spotId) {
		ServiceResponse<Spot> response = spotService.getSpot(AEUtils.ADMIN_ACESS_ID, spotId, "contract");
		Spot spot = response.getReponse();

		if (spot == null) {
			logger.error("Can't find a spot using the given id [SpotId: {}]", spotId);
			return errorAdModel("INVALID_SPOT_ID");
		}

		return buildUsingSpot(spot);
	}

	private AdModel buildUsingSpot(Spot spot) {
		String adId = getAdId(spot, spot.getContract());

		Ad ad = adService.getAdById(adId).getReponse();
		if (ad == null) {
			logger.error("Can't find an Ad using the given id", adId);
			return errorAdModel("INVALID_AD_ID");
		}

		AdModel model = buildUsingAd(ad);

		model.setSpotId(spot.getId());
		model.setRedirectUrl(buildRedirectUrl("https://localhost:8080", UrlRedirectorController.REDIRECT_ENDPOINT,
				urlRedirecterService.mapRefUrl(spot.getId(), ad.getRefUrl())));
		return model;
	}

	/**
	 * @param spot
	 * @param contract
	 * @return the id of the Ad that will be used to create the {@link AdModel}
	 */
	private String getAdId(Spot spot, Contract contract) {
		if (contract != null && !contract.hasExpired() && hasContractOwnerBalanceToPayAd(contract)) {
			return contract.getAdId();
		}

		return spot.getFallbackAdId();
	}

	/**
	 * @param contract
	 * @return <code>true</code> if the {@link Contract#getCreatorId()} has
	 *         balance(money) to pay for the ad
	 */
	private boolean hasContractOwnerBalanceToPayAd(Contract contract) {
		// TODO notify contract owner that he doesn't have balance
		return accountService.hasAccountBalance(contract.getCreatorId(), contract.convertPaymentValueToDotNotation());
	}

	private AdModel buildUsingAd(Ad ad) {
		if (ad.getType() == AdType.TEXT) {
			return new AdModel(adTemplateService.assembleUsingTextAd((TextAd) ad));
		} else if (ad.getType() == AdType.IMAGE) {
			return new AdModel(adTemplateService.assembleUsingImageAd((ImageAd) ad));
		}

		logger.error("The Ad type is not valid [id: {}, type: {}]", ad.getId(), ad.getType());
		return errorAdModel("INVALID_AD_TYPE");
	}

	private AdModel errorAdModel(String error) {
		AdModel model = new AdModel("");
		model.setError(error);

		return model;
	}

	private String buildRedirectUrl(String path, String redirectEndpoint, String id) {
		return path + redirectEndpoint + "/" + id;
	}
}
