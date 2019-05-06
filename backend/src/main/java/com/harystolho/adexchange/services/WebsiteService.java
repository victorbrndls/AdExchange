package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.website.WebsiteRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class WebsiteService {

	private WebsiteRepository websiteRepository;

	public WebsiteService(WebsiteRepository websiteRepository) {
		this.websiteRepository = websiteRepository;
	}

	public ServiceResponse<List<Website>> getWebsites() {
		return ServiceResponse.ok(websiteRepository.getWebsites());
	}

	public ServiceResponse<Website> getWebsiteById(String id) {
		return ServiceResponse.ok(websiteRepository.getById(id));
	}

	public ServiceResponse<Website> createWebsite(String accountId, String id, String name, String url, String logoURL,
			String description, String cats) {
		String[] categories = cats.split(",");

		if (!verifyWebsiteCreationFields(name, url, logoURL, description, categories))
			return ServiceResponse.fail("Invalid fields");

		Website website = new Website(accountId, url);

		if (id != null) // If the id is not null this means the user is editing an existing website
			website = websiteRepository.getById(id);

		website.setName(name);
		website.setLogoUrl(logoURL);
		website.setDescription(description);
		website.setCategories(categories);

		return ServiceResponse.ok(websiteRepository.saveWebsite(website));
	}

	public ServiceResponse<Website> deleteWebsite(String accountId, String id) {
		Website website = websiteRepository.getById(id);

		if (website == null)
			return ServiceResponse.fail("INVALID_WEBSITE_ID");

		if (!website.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		websiteRepository.deleteById(id);

		return ServiceResponse.ok(null);
	}

	/**
	 * @param accountId
	 * @param websiteId
	 * @return TRUE if the {accountId} was the account who created the website
	 */
	public boolean accountOwnsWebsite(String accountId, String websiteId) {
		Website website = websiteRepository.getById(websiteId);

		if (website != null)
			return website.getAccountId().equals(accountId);

		return false;
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
		for (String category : categories) {
			if (!StringUtils.hasText(category))
				return false;

			if (!category.toUpperCase().equals(category))
				return false;
		}
		if (categories.length > 3)
			return false;

		return true;
	}

	public String getAccountIdUsingWebsiteId(String id) {
		Website website = websiteRepository.getById(id);
		return website.getAccountId();
	}

}
