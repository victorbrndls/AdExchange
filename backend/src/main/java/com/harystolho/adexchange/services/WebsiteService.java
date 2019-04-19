package com.harystolho.adexchange.services;

import java.util.List;

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

	public Pair<ServiceResponse, List<Website>> getWebsites() {
		return Pair.of(ServiceResponse.OK, websiteRepository.getWebsites());
	}

	public Pair<ServiceResponse, Website> getWebsiteById(String id) {
		return Pair.of(ServiceResponse.OK, websiteRepository.getWebsiteById(id));
	}

	public Pair<ServiceResponse, Website> createWebsite(String name, String url, String logoURL, String description) {
		if (!verifyWebsiteCreationFields(name, url, logoURL, description))
			return Pair.of(ServiceResponse.FAIL, null);

		Website website = new Website("05b1aedc-e44e-4cea-85a7-0d2b594f4363", url);
		website.setName(name);
		website.setLogoUrl(logoURL);
		website.setDescription(description);

		Pair<RepositoryResponse, Website> response = websiteRepository.saveWebsite(website);

		if (response.getFist() == RepositoryResponse.CREATED)
			return Pair.of(ServiceResponse.OK, response.getSecond());

		return Pair.of(ServiceResponse.FAIL, null);
	}

	private boolean verifyWebsiteCreationFields(String name, String url, String logoUrl, String description) {
		return true; // TODO verify website creation fields
	}

}
