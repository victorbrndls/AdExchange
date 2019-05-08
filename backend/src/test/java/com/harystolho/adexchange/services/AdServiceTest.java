package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.TextAd;
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
	private static final String validColor = "#fff";

	@Test
	public void createAdWithInvalidTypeShouldReturnError() {
		for (String type : Arrays.asList("", "   ", "", "text", "life", "IM AGE", "_TEXT_", " TEXT")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, null, validName, type,
					validRefUrl, "", "", "", "");

			assertEquals("Type: " + type, ServiceResponseType.INVALID_AD_TYPE, response.getErrorType());
		}
	}

	@Test
	public void createAdWithInvalidRefUrlShouldReturnError() {
		for (String url : Arrays.asList("", " ", "https://", "http://", "http", " https://", "\">https", "some s",
				"https://url .com", "<input>", "ftp://", "://")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, null, validName, "TEXT", url, "",
					"", "", "");

			assertEquals("Ref Url: " + url, ServiceResponseType.INVALID_AD_REF_URL, response.getErrorType());
		}
	}

	@Test
	public void createAdWithInvalidBgColorShouldReturnError() {
		for (String color : Arrays.asList("", "#", " ", "", "ffw#", "# ", "# 57f", "__", "a-z", "$#$")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, null, validName, "TEXT",
					validRefUrl, validText, color, "#fff", "");

			assertEquals("Color: " + color, ServiceResponseType.INVALID_AD_BG_COLOR, response.getErrorType());
		}
	}

	@Test
	public void updateAdWithInvalidAdId() {
		ServiceResponse<Ad> response = adService.createOrUpdateAd(validAccountId, "null-ad-id", validName, "TEXT",
				validRefUrl, validText, validColor, validColor, null);

		assertEquals(ServiceResponseType.INVALID_AD_ID, response.getErrorType());
	}

	@Test
	public void updateAdThatDoesntBelongToUser() {
		TextAd ad = new TextAd();
		ad.setAccountId("abc");
		Mockito.when(adRepository.getAdById("123")).thenReturn(ad);

		ServiceResponse<Ad> response = adService.createOrUpdateAd("acc-gigi", "123", validName, "TEXT", validRefUrl,
				validText, validColor, validColor, null);

		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

	@Test
	public void updateAd() {
		TextAd ad = new TextAd();
		ad.setId("789");
		ad.setAccountId("acc-kiki");
		ad.setName("OldName");
		Mockito.when(adRepository.getAdById("789")).thenReturn(ad);

		Mockito.when(adRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

		ServiceResponse<Ad> response = adService.createOrUpdateAd("acc-kiki", "789", "newName", "TEXT", validRefUrl,
				validText, validColor, validColor, null);

		assertEquals("newName", response.getReponse().getName());
		assertEquals("789", response.getReponse().getId());
	}
}
