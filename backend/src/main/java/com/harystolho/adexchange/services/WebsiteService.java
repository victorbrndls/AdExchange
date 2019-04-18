package com.harystolho.adexchange.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

@Service
public class WebsiteService {

	private WebsiteRepository websiteRepository;

	public WebsiteService(WebsiteRepository websiteRepository) {
		this.websiteRepository = websiteRepository;
	}

	public Pair<ServiceResponse, Website> createWebsite(String url, String logoURL, String description) {
		if (!verifyWebsiteCreationFields(url, logoURL, description))
			return Pair.of(ServiceResponse.FAIL, null);

		Website website = new Website(url);
		website.setLogoUrl(logoURL);
		website.setDescription(description);

		Pair<RepositoryResponse, Website> response = websiteRepository.saveWebsite(website);

		if (response.getFist() == RepositoryResponse.CREATED)
			return Pair.of(ServiceResponse.OK, response.getSecond());

		return Pair.of(ServiceResponse.FAIL, null);
	}

	private boolean verifyWebsiteCreationFields(String url, String logoUrl, String description) {
		return true; // TODO verify website creation fields
	}

}
