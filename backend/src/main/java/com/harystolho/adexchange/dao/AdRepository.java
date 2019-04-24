package com.harystolho.adexchange.dao;

import java.util.List;

import com.harystolho.adexchange.models.ads.Ad;

public interface AdRepository {

	Ad save(Ad ad);

	List<Ad> getAdsByAccountId();

	Ad getAdById(String id);

}
