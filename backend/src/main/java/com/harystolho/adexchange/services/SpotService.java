package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.repositories.spot.SpotRepository;

@Service
public class SpotService {

	private SpotRepository spotRepository;

	@Autowired
	private SpotService(SpotRepository spotRepository) {
		this.spotRepository = spotRepository;
	}

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
		spot.setAdId(adId);

		return ServiceResponse.ok(spotRepository.save(spot));
	}

	public ServiceResponse<Spot> getSpot(String accountId, String id) {
		Spot spot = spotRepository.getById(id);

		if (spot == null)
			return ServiceResponse.fail("There is not spot with that id");

		if (!spot.getAccountId().equals(accountId))
			return ServiceResponse.unauthorized();

		return ServiceResponse.ok(spot);
	}

	public ServiceResponse<List<Spot>> getSpotsByAccountId(String accountId) {
		return ServiceResponse.ok(spotRepository.getByAccountId(accountId));
	}

}
