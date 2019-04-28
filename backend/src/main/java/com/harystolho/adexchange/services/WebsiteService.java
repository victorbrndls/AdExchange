package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.RepositoryResponse;
import com.harystolho.adexchange.repositories.WebsiteRepository;
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

	public Pair<ServiceResponse, Website> createWebsite(String accountId, String name, String url, String logoURL,
			String description, String cats) {
		String[] categories = cats.split(",");

		if (!verifyWebsiteCreationFields(name, url, logoURL, description, categories))
			return Pair.of(ServiceResponse.FAIL, null);

		Website website = new Website(accountId, url);
		website.setName(name);
		website.setLogoUrl(logoURL);
		website.setDescription(description);
		website.setCategories(categories);

		Pair<RepositoryResponse, Website> response = websiteRepository.saveWebsite(website);

		if (response.getFist() == RepositoryResponse.CREATED)
			return Pair.of(ServiceResponse.OK, response.getSecond());

		return Pair.of(ServiceResponse.FAIL, null);
	}

	private boolean verifyWebsiteCreationFields(String name, String url, String logoUrl, String description,
			String[] categories) {
		if (description.length() < 10)
			return false;

		if (url.length() < 5)
			return false;

		if (!verifyCategories(categories))
			return false;

		return true;
	}

	/**
	 * Valid categories are UPPERCASE
	 * 
	 * @param categories
	 * @return TRUE if the categories are valid
	 */
	private boolean verifyCategories(String[] categories) {
		for (String category : categories)
			if (!category.toUpperCase().equals(category))
				return false;

		if (categories.length > 3)
			return false;

		return true;
	}

	public String getAccountIdUsingWebsiteId(String id) {
		Website website = websiteRepository.getWebsiteById(id);
		return website.getAccountId();
	}

}
