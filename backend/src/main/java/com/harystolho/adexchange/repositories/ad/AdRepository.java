package com.harystolho.adexchange.repositories.ad;

import java.util.List;

import com.harystolho.adexchange.models.ads.Ad;

public interface AdRepository {

	Ad save(Ad ad);

	List<Ad> getAdsByAccountId(String accountId);

	Ad getAdById(String id);

	List<Ad> getAdsById(List<String> ids);

	void removeById(String id);

}
