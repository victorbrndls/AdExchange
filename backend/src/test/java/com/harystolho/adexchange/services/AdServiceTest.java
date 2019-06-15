package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.controllers.models.AdBuilderModel;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.models.ads.ImageAd;
import com.harystolho.adexchange.models.ads.TextAd;
import com.harystolho.adexchange.models.ads.TextAd.TextAlignment;
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
	private static final String validTextAlignment = "LEFT";
	private static final String validColor = "#fff";

	@Test
	public void createAdWithInvalidTypeShouldReturnError() {
		for (String type : Arrays.asList("", "   ", "", "text", "life", "IM AGE", "_TEXT_", " TEXT")) {
			;
			ServiceResponse<Ad> response = adService
					.createOrUpdateAd(new AdBuilderModel().setAccountId(validAccountId).setName(validName).setType(type)
							.setRefUrl(validRefUrl).setText(validText).setTextAlignment(validTextAlignment));

			assertEquals("Type: " + type, ServiceResponseType.INVALID_AD_TYPE, response.getErrorType());
		}
	}

	@Test
	public void createAdWithInvalidRefUrlShouldReturnError() {
		for (String url : Arrays.asList("", " ", "https://", "http://", "http", " https://", "\">https", "some s",
				"https://url .com", "<input>", "ftp://", "://")) {
			ServiceResponse<Ad> response = adService
					.createOrUpdateAd(new AdBuilderModel().setAccountId(validAccountId).setName(validName)
							.setType("TEXT").setRefUrl(url).setText(validText).setTextAlignment(validTextAlignment));

			assertEquals("Ref Url: " + url, ServiceResponseType.INVALID_AD_REF_URL, response.getErrorType());
		}
	}

	@Test
	public void createAdWithInvalidBgColorShouldReturnError() {
		for (String color : Arrays.asList("", "#", " ", "", "ffw#", "# ", "# 57f", "__", "a-z", "$#$")) {
			ServiceResponse<Ad> response = adService.createOrUpdateAd(new AdBuilderModel().setAccountId(validAccountId)
					.setName(validName).setType("TEXT").setRefUrl(validRefUrl).setText(validText)
					.setTextAlignment(validTextAlignment).setBgColor(color));

			assertEquals("Color: " + color, ServiceResponseType.INVALID_AD_BG_COLOR, response.getErrorType());
		}
	}

	@Test
	public void createAdWithInvalidTextAlignment_ShouldFail() {
		ServiceResponse<Ad> response = adService
				.createOrUpdateAd(new AdBuilderModel().setAccountId(validAccountId).setName(validName).setType("TEXT")
						.setRefUrl(validRefUrl).setText(validText).setTextAlignment("DIA(F*&S"));

		assertEquals(ServiceResponseType.INVALID_AD_TEXT_ALIGNMENT, response.getErrorType());
	}

	@Test
	public void createAdWithInvalidTextSize_ShouldFail() {
		ServiceResponse<Ad> response = adService
				.createOrUpdateAd(new AdBuilderModel().setName(validName).setType("TEXT").setRefUrl(validRefUrl)
						.setText(validText).setTextAlignment(validTextAlignment).setTextSize(-1));

		assertEquals(ServiceResponseType.INVALID_AD_TEXT_SIZE, response.getErrorType());
	}

	@Test
	public void updateAdWithInvalidAdId() {
		ServiceResponse<Ad> response = adService.createOrUpdateAd(new AdBuilderModel().setAccountId(validAccountId)
				.setId("invalid-ad-id").setName(validName).setType("TEXT").setRefUrl(validRefUrl).setText(validText)
				.setTextAlignment(validTextAlignment));

		assertEquals(ServiceResponseType.INVALID_AD_ID, response.getErrorType());
	}

	@Test
	public void updateAdThatDoesntBelongToUser() {
		TextAd ad = new TextAd();
		ad.setAccountId("abc");
		Mockito.when(adRepository.getAdById("123")).thenReturn(ad);

		ServiceResponse<Ad> response = adService.createOrUpdateAd(
				new AdBuilderModel().setAccountId("acc-gigi").setId("123").setName(validName).setType("TEXT")
						.setRefUrl(validRefUrl).setText(validText).setTextAlignment(validTextAlignment));

		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

	@Test
	public void createTextAd_ShouldWork() {
		Mockito.when(adRepository.save(Mockito.any())).thenAnswer((inv) -> inv.getArgument(0));

		ServiceResponse<Ad> response = adService.createOrUpdateAd(
				new AdBuilderModel().setAccountId("acc").setName("Texto").setType("TEXT").setRefUrl(validRefUrl)
						.setText("Life is cool").setTextAlignment("RIGHT").setBgColor("#fff").setTextColor("#014"));

		assertEquals(ServiceResponseType.OK, response.getErrorType());

		TextAd ad = (TextAd) response.getReponse();
		assertEquals("Texto", ad.getName());
		assertEquals("Life is cool", ad.getText());
		assertEquals(TextAlignment.RIGHT, ad.getTextAlignment());
		assertEquals("#014", ad.getTextColor());
		assertEquals("#fff", ad.getBgColor());
	}

	@Test
	public void createImageAd_ShouldWork() {
		Mockito.when(adRepository.save(Mockito.any())).thenAnswer((inv) -> inv.getArgument(0));

		ServiceResponse<Ad> response = adService.createOrUpdateAd(new AdBuilderModel().setName("Unimage")
				.setType("IMAGE").setRefUrl(validRefUrl).setImageUrl("http://localhost:8080.png"));

		assertEquals(ServiceResponseType.OK, response.getErrorType());

		ImageAd ad = (ImageAd) response.getReponse();
		assertEquals("Unimage", ad.getName());
		assertEquals("http://localhost:8080.png", ad.getImageUrl());
	}

	@Test
	public void updateAd() {
		TextAd ad = new TextAd();
		ad.setId("789");
		ad.setAccountId("acc-kiki");
		ad.setName("OldName");
		Mockito.when(adRepository.getAdById("789")).thenReturn(ad);

		Mockito.when(adRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

		ServiceResponse<Ad> response = adService.createOrUpdateAd(new AdBuilderModel().setAccountId("acc-kiki")
				.setId("789").setName("newName").setType("TEXT").setRefUrl(validRefUrl).setText(validText)
				.setTextAlignment(validTextAlignment).setTextColor(validColor).setBgColor(validColor));

		assertEquals("newName", response.getReponse().getName());
		assertEquals("789", response.getReponse().getId());
	}
}
