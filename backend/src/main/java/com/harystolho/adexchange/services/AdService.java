package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Ad;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AdService {

	public AdService() {
	}

	public Pair<ServiceResponse, Ad> createAd(String name) {
		return null;
	}

}
