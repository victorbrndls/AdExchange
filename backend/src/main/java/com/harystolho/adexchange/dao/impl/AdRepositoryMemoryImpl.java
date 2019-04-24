package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.harystolho.adexchange.dao.AdRepository;
import com.harystolho.adexchange.models.ads.Ad;

public class AdRepositoryMemoryImpl implements AdRepository {

	private List<Ad> ads;

	public AdRepositoryMemoryImpl() {
		ads = new ArrayList<>();
	}

	@Override
	public Ad save(Ad ad) {
		ad.setId(UUID.randomUUID().toString());

		ads.add(ad);

		return ad;
	}

}
