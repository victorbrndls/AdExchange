package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.website.WebsiteRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class WebsiteServiceTest {

	@InjectMocks
	WebsiteService websiteService;

	@Mock
	WebsiteRepository websiteRepository;

	@Test
	public void createWebsiteWithInvalidURL() {
		ServiceResponse<Website> response = websiteService.createWebsite("", "some name", "http", "",
				"this is a very big description for this website", "OTHER");

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void createWebsiteWithNoDescription() {
		ServiceResponse<Website> response = websiteService.createWebsite("", "some name", "https://ad-exchange.com", "",
				"to small", "OTHER");

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void createWebsiteWithValidFields() {
		String url = "https://ad-exchange.com";
		String description = "this is a very big description for this website";

		Website website = new Website(null, url);
		website.setDescription(description);

		Mockito.when(websiteRepository.saveWebsite(Mockito.any())).thenReturn(website);

		ServiceResponse<Website> response = websiteService.createWebsite("", "some name", url, "", description,
				"OTHER");

		assertEquals(ServiceResponseType.OK, response.getErrorType());

		assertEquals(url, response.getReponse().getUrl());
		assertEquals(description, response.getReponse().getDescription());
	}

	@Test
	public void createWebsiteWithInvalidCategory() {
		ServiceResponse<Website> response = websiteService.createWebsite("", "some name", "https://ad-exchange.com", "",
				"some description to use", "lower");

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void createWebsiteWithValidCategory() {
		Mockito.when(websiteRepository.saveWebsite(Mockito.any())).thenReturn(null);

		ServiceResponse<Website> response = websiteService.createWebsite("", "some name", "https://ad-exchange.com", "",
				"some description to use", "UPPER");

		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

}
