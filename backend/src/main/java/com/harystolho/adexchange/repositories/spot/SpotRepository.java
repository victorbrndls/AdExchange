package com.harystolho.adexchange.repositories.spot;

import com.harystolho.adexchange.models.Spot;

public interface SpotRepository {

	Spot save(Spot spot);

	Spot getById(String id);
	
}
