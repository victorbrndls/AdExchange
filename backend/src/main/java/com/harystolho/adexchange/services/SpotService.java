package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adServer.services.AdModelServerService;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.repositories.spot.SpotRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class SpotService {

	private SpotRepository spotRepository;

	private ContractService contractService;
	private AdModelServerService adModelServerService;

	@Autowired
	private SpotService(SpotRepository spotRepository, ContractService contractService) {
		this.spotRepository = spotRepository;
		this.contractService = contractService;
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

		if (embed.contains("contract")) {
			ServiceResponse<Contract> contractResponse = contractService.getContractById(accountId,
					spot.getContractId());

			if (contractResponse.getErrorType() != ServiceResponseType.OK)
				return ServiceResponse.fail("Can't get contract using Spot contractId");

			spot.setContract(contractResponse.getReponse());
		}

		return ServiceResponse.ok(spot);
	}

	public ServiceResponse<List<Spot>> getSpotsByAccountId(String accountId) {
		return ServiceResponse.ok(spotRepository.getByAccountId(accountId));
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

	// Inject using setter to break dependency cycle
	@Autowired
	public void setAdModelServerService(AdModelServerService adModelServerService) {
		this.adModelServerService = adModelServerService;
	}

}
