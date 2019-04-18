package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.services.WebsiteService;
import com.harystolho.adexchange.utils.Pair;

@Service
public class WebsiteServiceMemoryImpl implements WebsiteRepository {

	private List<Website> websites;

	public WebsiteServiceMemoryImpl() {
		websites = new ArrayList<>();
	}

	@Override
	public Pair<RepositoryResponse, Website> saveWebsite(Website website) {
		websites.add(website);

		return Pair.of(RepositoryResponse.CREATED, website);
	}

}
