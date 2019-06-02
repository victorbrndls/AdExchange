package com.harystolho.adserver.services.admodel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
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
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.controllers.UrlRedirectorController;
import com.harystolho.adserver.data.AdModelDataCache;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.templates.AdTemplateService;

/**
 * Builds {@link AdModel}
 * 
 * @author Harystolho
 *
 */
@Service
public class AdModelFactory {

	private static final Logger logger = LogManager.getLogger();

	private SpotService spotService;
	private AdService adService;
	private UrlRedirecterService urlRedirecterService;
	private AdTemplateService adTemplateService;
	private AccountService accountService;
	private AdModelDataCache adModelCache;

	public enum AdSource { // If the adModel was created using a contract or a spot fallback ad
		CONTRACT, SPOT_FALLBACK
	}

	@Autowired
	private AdModelFactory(SpotService spotService, AdService adService, UrlRedirecterService urlRedirecterService,
			AdTemplateService adTemplateService, AccountService accountService, AdModelDataCache dataCacheService) {
		this.spotService = spotService;
		this.adService = adService;
		this.urlRedirecterService = urlRedirecterService;
		this.adTemplateService = adTemplateService;
		this.accountService = accountService;
		this.adModelCache = dataCacheService;
	}

	public AdModel buildUsingSpotId(String spotId) {
		ServiceResponse<Spot> response = spotService.getSpot(AEUtils.ADMIN_ACCESS_ID, spotId, "contract");
		Spot spot = response.getReponse();

		if (spot == null) {
			logger.error("Can't find a spot using the given id [SpotId: {}]", spotId);
			return errorAdModel("INVALID_SPOT_ID");
		}

		return buildUsingSpot(spot);
	}

	private AdModel buildUsingSpot(Spot spot) {
		Pair<String, AdSource> adPair = getAdId(spot, spot.getContract());

		Ad ad = adService.getAdById(adPair.getFist()).getReponse();

		if (ad == null)
			return errorAdModel("INVALID_AD_ID");

		AdModel model = buildUsingAd(ad);
		model.setAdSource(adPair.getSecond());

		model.setSpotId(spot.getId());
		model.setRedirectUrl(buildRedirectUrl("https://localhost:8080", UrlRedirectorController.REDIRECT_ENDPOINT,
				urlRedirecterService.mapRefUrl(spot.getId(), ad.getRefUrl(), adPair.getSecond())));
		return model;
	}

	/**
	 * @param spot
	 * @param contract
	 * @return the contract ad id if the contract is valid or the spot fallback ad
	 *         id
	 */
	private Pair<String, AdSource> getAdId(Spot spot, Contract contract) {
		if (contract != null && isContractValid(contract)) {
			adModelCache.update(spot, contract);

			return Pair.of(contract.getAdId(), AdSource.CONTRACT);
		}

		return Pair.of(spot.getFallbackAdId(), AdSource.SPOT_FALLBACK);
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

	private boolean isContractValid(Contract contract) {
		if (contract.hasExpired())
			return false;

		// There is no need to check the balance for PAY_ONCE contracts because they
		// have already been payed
		if (contract.getPaymentMethod() == PaymentMethod.PAY_PER_CLICK
				|| contract.getPaymentMethod() == PaymentMethod.PAY_PER_VIEW)
			if (!hasContractOwnerBalanceToPayAd(contract))
				return false;

		return true;
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

}
