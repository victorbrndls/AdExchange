package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adserver.services.AdModelService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.spots.events.SpotUpdatedEvent;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.repositories.spot.SpotRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class SpotService {

	private SpotRepository spotRepository;

	private ContractService contractService;
	private AdService adService;
	private EventDispatcher eventDispatcher;

	private SpotService(SpotRepository spotRepository, ContractService contractService, AdService adService,
			EventDispatcher eventDispatcher) {
		this.spotRepository = spotRepository;
		this.contractService = contractService;
		this.adService = adService;
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * Creates a {@link Spot} if the {id} is null or updates an existing one if the
	 * {id} is not null
	 */
	public ServiceResponse<Spot> createOrUpdateSpot(String accountId, String id, String name, String contractId,
			String adId) {
		ServiceResponseType response = verifyFields(name);
		if (response != ServiceResponseType.OK)
			return ServiceResponse.error(response);

		ServiceResponse<Spot> spotResponse = createOrGetSpot(id, accountId);
		if (spotResponse.getErrorType() != ServiceResponseType.OK)
			return ServiceResponse.error(spotResponse.getErrorType());

		Spot spot = spotResponse.getReponse();

		spot.setAccountId(accountId);
		spot.setName(name);
		spot.setContractId(contractId);
		spot.setFallbackAdId(adId);

		Spot saved = spotRepository.save(spot);

		// If the Spot has changed, the adModel cache has the remove the old one
		if (id != null)
			eventDispatcher.dispatch(new SpotUpdatedEvent(spot.clone()));

		return ServiceResponse.ok(saved);
	}

	/**
	 * @param spotId
	 * @param accountId
	 * @return if there is a {@link Spot} with the {spotId} it gets returned,
	 *         otherwise a new {@link Spot} is created
	 */
	private ServiceResponse<Spot> createOrGetSpot(String spotId, String accountId) {
		if (spotId != null) {
			Spot existingSpot = spotRepository.getById(spotId);

			if (existingSpot != null) {
				if (!existingSpot.isAuthorized(accountId))
					return ServiceResponse.unauthorized();

				return ServiceResponse.ok(existingSpot);
			}
		}

		return ServiceResponse.ok(new Spot());
	}

	public ServiceResponse<Spot> getSpot(String accountId, String id, String embed) {
		Spot spot = spotRepository.getById(id);

		if (spot == null)
			return ServiceResponse.fail("There is not spot with that id");

		if (!spot.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		if (embed.contains("contract"))
			embedContract(spot);

		return ServiceResponse.ok(spot);
	}

	/**
	 * @param accountId
	 * @param embed     ["contract", "ad"]
	 * @return
	 */
	public ServiceResponse<List<Spot>> getSpotsByAccountId(String accountId, String embed) {
		List<Spot> spots = spotRepository.getByAccountId(accountId);

		spots.forEach((spot) -> {
			if (embed.contains("contract"))
				embedContract(spot);

			if (embed.contains("ad"))
				embedFallbackAd(spot);
		});

		return ServiceResponse.ok(spots);
	}

	public ServiceResponse<Spot> deleteSpot(String accountId, String id) {
		Spot spot = spotRepository.getById(id);

		if (spot == null)
			return ServiceResponse.fail("There is not Spot with that id");

		if (!spot.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		spotRepository.deleteById(id);
		return ServiceResponse.ok(null);
	}

	private ServiceResponseType verifyFields(String name) {
		if (StringUtils.isEmpty(name))
			return ServiceResponseType.INVALID_SPOT_NAME;

		return ServiceResponseType.OK;
	}

	private void embedFallbackAd(Spot spot) {
		ServiceResponse<Ad> response = adService.getAdById(spot.getFallbackAdId());

		spot.setFallbackAd(response.getReponse());
	}

	private void embedContract(Spot spot) {
		ServiceResponse<Contract> response = contractService.getContractById(spot.getAccountId(), spot.getContractId());

		spot.setContract(response.getReponse());
	}

}
