package com.harystolho.adexchange.repositories.spot;

import java.util.List;

import com.harystolho.adexchange.models.Spot;

public interface SpotRepository {

	Spot save(Spot spot);

	Spot getById(String id);

	List<Spot> getByAccountId(String accountId);

	void deleteById(String id);
	
}
