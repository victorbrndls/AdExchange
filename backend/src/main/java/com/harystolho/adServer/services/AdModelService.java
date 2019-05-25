package com.harystolho.adServer.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@Service
public class AdModelService {

	private static final Logger logger = LogManager.getLogger();

	private final Map<String, DataCache> accountIdToDataCache;

	private SpotService spotService;
	private AdService adService;
	private UrlRedirecterService urlRedirecterService;
	private AdTemplateService adTemplateService;
	private AccountService accountService;
	private AdServerService adServerService;

	@Autowired
	private AdModelService(SpotService spotService, AdService adService, UrlRedirecterService urlRedirecterService,
			AdTemplateService adTemplateService, AccountService accountService, AdServerService adServerService) {
		this.spotService = spotService;
		this.adService = adService;
		this.urlRedirecterService = urlRedirecterService;
		this.adTemplateService = adTemplateService;
		this.accountService = accountService;
		this.adServerService = adServerService;

		this.accountIdToDataCache = new HashMap<>();
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
	private String getAdId(Spot spot, Contract contract) { // TODO change the method bc some ads are fixed price
		if (contract != null && !contract.hasExpired() && hasContractOwnerBalanceToPayAd(contract)) {
			updateDataCacheEntry(spot, contract);

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

	/**
	 * This method is called when the account balance changes and for that reason
	 * the user may not jave enough balance to pay for more ads. This method updates
	 * all the spots that are bound to contracts owned by the {accountId} and
	 * removes the ones that the user can't pay for
	 * 
	 * @param accountId
	 */
	public void updateSpotsAdvertisedByUser(String accountId) {
		DataCache dc = accountIdToDataCache.get(accountId);

		if (dc == null)
			return; // There are no ads being displayed that are payed by this user

		Set<Contract> contractsToBeRemoved = new HashSet<>();
		Set<Spot> spotsToBeRemoved = new HashSet<>();

		// Contracts that the user doesn't have balance to pay
		for (Contract c : dc.getContracts()) {
			if (!accountService.hasAccountBalance(accountId, c.convertPaymentValueToDotNotation())) {
				contractsToBeRemoved.add(c);
			}
		}

		// Spots that are bound to contracts that the user doesn't have balance to pay
		for (Spot s : dc.getSpots()) {
			for (Contract c : contractsToBeRemoved) {
				if (c.getId().equals(s.getContractId())) {
					spotsToBeRemoved.add(s);
					continue;
				}
			}
		}

		dc.removeContracts(contractsToBeRemoved);
		dc.removeSpots(spotsToBeRemoved);

		spotsToBeRemoved.forEach(s -> adServerService.updateSpot(s));
	}

	protected void updateDataCacheEntry(Spot spot, Contract contract) {
		DataCache dc = accountIdToDataCache.getOrDefault(contract.getCreatorId(), new DataCache());

		dc.addContract(contract);
		dc.addSpot(spot);

		accountIdToDataCache.put(contract.getCreatorId(), dc);
	}

	/**
	 * Caches the contracts the belong to an user and the spots that are bound to
	 * these contracts
	 * 
	 * @author Harystolho
	 *
	 */
	private class DataCache {
		private Set<Contract> contracts;
		private Set<Spot> spots;

		public DataCache() {
			contracts = new HashSet<>();
			spots = new HashSet<>();
		}

		public Set<Contract> getContracts() {
			return contracts;
		}

		public void addContract(Contract contract) {
			this.contracts.add(contract);
		}

		public void removeContracts(Collection<Contract> contracts) {
			this.contracts.removeAll(contracts);
		}

		public Set<Spot> getSpots() {
			return spots;
		}

		public void addSpot(Spot spot) {
			this.spots.add(spot);
		}

		public void removeSpots(Collection<Spot> spots) {
			this.spots.removeAll(spots);
		}

	}
}
