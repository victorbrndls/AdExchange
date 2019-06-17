package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.controllers.models.WebsiteBuilderModel;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.repositories.website.WebsiteRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class WebsiteServiceTest {

	@InjectMocks
	WebsiteService websiteService;

	@Mock
	WebsiteRepository websiteRepository;

	private static final WebsiteBuilderModel validModel = new WebsiteBuilderModel().setAccountId("acc1")
			.setName("Valid Name").setDescription("Some valid description").setUrl("https://adnamic.com")
			.setLogoUrl("https://adnamic.png").setMonthlyImpressions("23").setCategories("BUSINESS,LIFE");

	@Test
	public void createWebsiteWithInvalidURL() {
		ServiceResponse<Website> response = websiteService
				.createWebsite(validModel.clone().setUrl("hxxp://intenet.com"));

		assertEquals(ServiceResponseType.INVALID_WEBSITE_URL, response.getErrorType());
	}

	@Test
	public void createWebsiteWithNoDescription() {
		ServiceResponse<Website> response = websiteService.createWebsite(validModel.clone().setDescription(""));

		assertEquals(ServiceResponseType.INVALID_WEBSITE_DESCRIPTION, response.getErrorType());
	}

	@Test
	public void createWebsiteWithValidFields() {
		ServiceResponse<Website> response = websiteService.createWebsite(validModel);

		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void createWebsiteWithInvalidCategory() {
		ServiceResponse<Website> response = websiteService.createWebsite(validModel.clone().setCategories("asdsa"));

		assertEquals(ServiceResponseType.INVALID_WEBSITE_CATEGORIES, response.getErrorType());
	}

	@Test
	public void createWebsiteWithMultipleInvalidCategories() {
		List<String> categories = Arrays.asList("UppeR", ".ddg9e", "lower", "OPER,LIFE,Drinks", "  ,  ");

		for (String cat : categories) {
			ServiceResponse<Website> response = websiteService.createWebsite(validModel.clone().setCategories(cat));

			assertEquals(String.format("Should return FAIL but didn't. Category:[%s]", cat),
					ServiceResponseType.INVALID_WEBSITE_CATEGORIES, response.getErrorType());
		}
	}

	@Test
	public void updateWebsiteShouldWork() {
		Website w = new Website("", "");
		w.setName("OldName");
		w.setAccountId("a1");
		Mockito.when(websiteRepository.getById("w1")).thenReturn(w);

		ServiceResponse<Website> response = websiteService.createWebsite(new WebsiteBuilderModel().setAccountId("a1")
				.setId("w1").setName("newName").setUrl("https://ad-exchang1e.com").setMonthlyImpressions("1")
				.setDescription("some long description").setCategories("UPPER"));

		assertEquals(ServiceResponseType.OK, response.getErrorType());
		assertEquals("newName", w.getName());
	}

	@Test
	public void updateWebsiteWithUnauthorizedAccount_ShouldFail() {
		Website w = new Website("", "");
		w.setAccountId("a23");
		Mockito.when(websiteRepository.getById("w2")).thenReturn(w);

		ServiceResponse<Website> response = websiteService
				.createWebsite(validModel.clone().setAccountId("b1").setId("w2"));

		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

}
