package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.dao.WebsiteRepository;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.utils.Pair;

public class WebsiteServiceTest {

	private static WebsiteService websiteService;
	private static WebsiteRepository websiteRepository;

	@BeforeClass
	public static void init() {
		websiteRepository = Mockito.mock(WebsiteRepository.class);

		websiteService = new WebsiteService(websiteRepository);
	}

	@Test
	public void createWebsiteWithInvalidURL() {
		Pair<ServiceResponse, Website> response = websiteService.createWebsite("invalid", "",
				"this is a very big description for this website");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void createWebsiteWithNoDescription() {
		Pair<ServiceResponse, Website> response = websiteService.createWebsite("https://ad-exchange.com", "", "to small");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void createWebsiteWithValidFields() {
		String url = "https://ad-exchange.com";
		String description = "this is a very big description for this website";

		Website website = new Website(url);
		website.setDescription(description);

		Mockito.when(websiteRepository.saveWebsite(Mockito.any()))
				.thenReturn(Pair.of(RepositoryResponse.CREATED, website));

		Pair<ServiceResponse, Website> response = websiteService.createWebsite(url, "", description);

		assertEquals(ServiceResponse.OK, response.getFist());

		assertEquals(url, response.getSecond().getUrl());
		assertEquals(description, response.getSecond().getDescription());
	}

}
