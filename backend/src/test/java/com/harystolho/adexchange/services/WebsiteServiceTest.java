package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.RepositoryResponse;
import com.harystolho.adexchange.repositories.WebsiteRepository;
import com.harystolho.adexchange.utils.Pair;

@RunWith(MockitoJUnitRunner.class)
public class WebsiteServiceTest {

	@InjectMocks
	WebsiteService websiteService;

	@Mock
	WebsiteRepository websiteRepository;

	@Test
	public void createWebsiteWithInvalidURL() {
		Pair<ServiceResponse, Website> response = websiteService.createWebsite("", "some name", "http", "",
				"this is a very big description for this website", "OTHER");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void createWebsiteWithNoDescription() {
		Pair<ServiceResponse, Website> response = websiteService.createWebsite("", "some name",
				"https://ad-exchange.com", "", "to small", "OTHER");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void createWebsiteWithValidFields() {
		String url = "https://ad-exchange.com";
		String description = "this is a very big description for this website";

		Website website = new Website(null, url);
		website.setDescription(description);

		Mockito.when(websiteRepository.saveWebsite(Mockito.any()))
				.thenReturn(Pair.of(RepositoryResponse.CREATED, website));

		Pair<ServiceResponse, Website> response = websiteService.createWebsite("", "some name", url, "", description,
				"OTHER");

		assertEquals(ServiceResponse.OK, response.getFist());

		assertEquals(url, response.getSecond().getUrl());
		assertEquals(description, response.getSecond().getDescription());
	}

	@Test
	public void createWebsiteWithInvalidCategory() {
		Pair<ServiceResponse, Website> response = websiteService.createWebsite("", "some name",
				"https://ad-exchange.com", "", "some description to use", "lower");

		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void createWebsiteWithValidCategory() {
		Mockito.when(websiteRepository.saveWebsite(Mockito.any()))
				.thenReturn(Pair.of(RepositoryResponse.CREATED, null));

		Pair<ServiceResponse, Website> response = websiteService.createWebsite("", "some name",
				"https://ad-exchange.com", "", "some description to use", "UPPER");

		assertEquals(ServiceResponse.OK, response.getFist());
	}

}
