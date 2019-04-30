package com.harystolho.adexchange.repositories.spot;

import java.util.List;

import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;

public interface SpotRepository {

	Spot save(Spot spot);

	Spot getById(String id);

	List<Spot> getByAccountId(String accountId);
	
}
