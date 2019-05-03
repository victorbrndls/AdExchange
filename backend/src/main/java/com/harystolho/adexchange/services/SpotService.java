package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.repositories.spot.SpotRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class SpotService {

	private SpotRepository spotRepository;

	private ContractService contractService;

	@Autowired
	private SpotService(SpotRepository spotRepository, ContractService contractService) {
		this.spotRepository = spotRepository;
		this.contractService = contractService;
	}

	/**
	 * Creates a {@link Spot} if the {id} is null or updates an existing one if the
	 * {id} is not null
	 */
	public ServiceResponse<Spot> createSpot(String accountId, String id, String name, String contractId, String adId) {
		Spot spot = new Spot();

		if (id != null) {
			Spot spt = spotRepository.getById(id);

			if (spt != null) {
				if (!spt.getAccountId().equals(accountId))
					return ServiceResponse.fail("The user doesn't own this spot");

				spot.setId(spt.getId());
			}
		}

		spot.setAccountId(accountId);
		spot.setName(name);
		spot.setContractId(contractId);
		spot.setFallbackAdId(adId);

		return ServiceResponse.ok(spotRepository.save(spot));
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

		if (!spot.getAccountId().equals(accountId))
			return ServiceResponse.unauthorized();

		spotRepository.deleteById(id);
		return ServiceResponse.ok(null);
	}

}
