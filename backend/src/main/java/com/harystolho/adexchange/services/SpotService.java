package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adServer.services.AdModelServerService;
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
	private AdModelServerService adModelServerService;

	private SpotService(SpotRepository spotRepository, ContractService contractService, AdService adService) {
		this.spotRepository = spotRepository;
		this.contractService = contractService;
		this.adService = adService;
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

		ServiceResponse<Spot> spotResponse = createOrGetSpotToUpdate(id, accountId);
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
			adModelServerService.updateSpot(saved);

		return ServiceResponse.ok(saved);
	}

	private ServiceResponse<Spot> createOrGetSpotToUpdate(String spotId, String accountId) {
		Spot spot = new Spot();

		if (spotId != null) {
			Spot spt = spotRepository.getById(spotId);

			if (spt != null) {
				if (!spt.isAuthorized(accountId))
					return ServiceResponse.unauthorized();

				spot.setId(spotId);
			}
		}

		return ServiceResponse.ok(spot);
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

	// Inject using setter to break dependency cycle
	@Autowired
	public void setAdModelServerService(AdModelServerService adModelServerService) {
		this.adModelServerService = adModelServerService;
	}

}
