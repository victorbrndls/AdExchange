package com.harystolho.adexchange.repositories;

import java.util.List;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.ads.Ad;

public interface AdRepository {

	Ad save(Ad ad);

	List<Ad> getAdsByAccountId(String accountId);

	/**
	 * @param id
	 * @return the {@link Ad} or <code>null</code>
	 */
	Ad getAdById(String id);

}
