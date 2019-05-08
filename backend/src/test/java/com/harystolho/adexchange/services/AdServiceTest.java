package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.repositories.ad.AdRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class AdServiceTest {

	@InjectMocks
	AdService adService;

	@Mock
	AdRepository adRepository;

	private static final String validAccountId = "1234";
	private static final String validName = "SomeName";
	private static final String validRefUrl = "http://www.ad-exchange.com/";
	private static final String validText = "Some text to put in the ad";
	private static final String validImageUrl = "http://www.some-image.com/image.png";

	@Test
	public void invalidTypeShouldReturnError() {
		for (String type : Arrays.asList("", "   ", "", "text", "life", "IM AGE", "_TEXT_", " TEXT")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, "", validName, type, validRefUrl,
					"", "", "", "");

			assertEquals("Type: " + type, ServiceResponseType.INVALID_AD_TYPE, response.getErrorType());
		}
	}

	@Test
	public void invalidRefUrlShouldReturnError() {
		for (String url : Arrays.asList("", " ", "https://", "http://", "http", " https://", "\">https", "some s",
				"https://url .com", "<input>", "ftp://", "://")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, "", validName, "TEXT", url, "",
					"", "", "");

			assertEquals("Ref Url: " + url, ServiceResponseType.INVALID_AD_REF_URL, response.getErrorType());
		}
	}

	@Test
	public void invalidBgColorShouldReturnError() {
		for (String color : Arrays.asList("", "#", " ", "", "ffw#", "# ", "# 57f", "__", "a-z", "$#$")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, "", validName, "TEXT",
					validRefUrl, validText, color, "#fff", "");

			assertEquals("Color: " + color, ServiceResponseType.INVALID_AD_BG_COLOR, response.getErrorType());
		}
	}

}
