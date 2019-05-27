package com.harystolho.adserver.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;

@Service
public class AdModelDataCache {

	// AccountId -> DataDache
	private final Map<String, AdModelData> container;

	public AdModelDataCache() {
		this.container = new HashMap<>();
	}

	public void update(Spot spot, Contract contract) {
		AdModelData dc = container.getOrDefault(contract.getCreatorId(), new AdModelData());

		dc.addContract(contract);
		dc.addSpot(spot);

		container.put(contract.getCreatorId(), dc);
	}

	public AdModelData get(String accountId) {
		return container.get(accountId);
	}

	public void remove(String accountId) {
		container.remove(accountId);
	}

}
