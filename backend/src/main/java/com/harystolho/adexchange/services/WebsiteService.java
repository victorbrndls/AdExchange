package com.harystolho.adexchange.services;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.controllers.models.WebsiteBuilderModel;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.website.WebsiteRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;

@Service
public class WebsiteService {

	private WebsiteRepository websiteRepository;

	public WebsiteService(WebsiteRepository websiteRepository) {
		this.websiteRepository = websiteRepository;
	}

	public ServiceResponse<List<Website>> getWebsites(String categories) {
		Set<String> categoriesSet = StringUtils.commaDelimitedListToSet(categories);

		if (categoriesSet.isEmpty())
			return ServiceResponse.ok(websiteRepository.getWebsites());

		return ServiceResponse.ok(websiteRepository.getWebsites(categoriesSet));
	}

	public ServiceResponse<Website> getWebsiteById(String id) {
		return ServiceResponse.ok(websiteRepository.getById(id));
	}

	public ServiceResponse<Website> createWebsite(WebsiteBuilderModel model) {
		ServiceResponseType response = verifyWebsiteCreationFields(model);
		if (response != ServiceResponseType.OK)
			return ServiceResponse.error(response);

		Website website = null;

		if (model.getId() != null) { // If the id is not null this means the user is editing an existing website
			website = websiteRepository.getById(model.getId());

			if (!website.isAuthorized(model.getAccountId()))
				return ServiceResponse.error(ServiceResponseType.UNAUTHORIZED);
		}

		if (website == null)
			website = new Website(model.getAccountId(), model.getUrl());

		website.setName(model.getName());
		website.setMonthlyImpressions(Integer.parseInt(model.getMonthlyImpressions()));
		website.setLogoUrl(model.getLogoURL());
		website.setDescription(model.getDescription());
		website.setCategories(StringUtils.commaDelimitedListToStringArray(model.getCategories()));

		return ServiceResponse.ok(websiteRepository.save(website));
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

	private ServiceResponseType verifyWebsiteCreationFields(WebsiteBuilderModel model) {
		if (model.getName().trim().length() < 2)
			return ServiceResponseType.INVALID_WEBSITE_NAME;

		if (!model.getUrl().matches(AEUtils.URL_REGEX))
			return ServiceResponseType.INVALID_WEBSITE_URL;

		try {
			Integer.parseInt(model.getMonthlyImpressions());
		} catch (Exception e) {
			return ServiceResponseType.INVALID_WEBSITE_IMPRESSIONS;
		}

		if (model.getDescription().trim().length() < 10)
			return ServiceResponseType.INVALID_WEBSITE_DESCRIPTION;

		if (!verifyCategories(StringUtils.commaDelimitedListToStringArray(model.getCategories())))
			return ServiceResponseType.INVALID_WEBSITE_CATEGORIES;

		return ServiceResponseType.OK;
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
