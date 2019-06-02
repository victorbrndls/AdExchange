package com.harystolho.adexchange.services.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.analytics.AnalyticsService;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.services.UrlRedirecterService.SpotData;
import com.harystolho.adserver.services.admodel.AdModelCacheService;
import com.harystolho.adserver.services.admodel.AdModelFactory.AdSource;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;

@RunWith(MockitoJUnitRunner.class)
public class SpotActionProcessorTest {

	@InjectMocks
	@Spy
	SpotActionProcessor spotActionProcessor;

	@Mock
	UrlRedirecterService urlRedirecterService;
	@Mock
	ContractPaymentService contractPaymentService;
	@Mock
	SpotService spotService;
	@Mock
	UserTrackerService userTrackerService;
	@Mock
	AnalyticsService analyticsService;
	@Mock
	AdModelCacheService adModelCacheService;

	@Test
	public void AdCreatedFromSpotFallbackShouldntBeBilled() {
		SpotData sd = new SpotData("as1", "ar1", AdSource.SPOT_FALLBACK);

		Mockito.when(urlRedirecterService.getSpotDataUsingRedirectId("ar1")).thenReturn(ServiceResponse.ok(sd));

		spotActionProcessor.processSpotClick("ar1", new Tracker(null, null));

		Mockito.verify(spotService, Mockito.never()).getSpot(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
	}

	@Test
	public void AdCreatedFromContractShouldProceedToBillingProcess() {
		SpotData sd = new SpotData("bs1", "br1", AdSource.CONTRACT);

		Mockito.when(urlRedirecterService.getSpotDataUsingRedirectId("br1")).thenReturn(ServiceResponse.ok(sd));

		Spot spot = new Spot();
		spot.setContractId("bc1");
		Mockito.when(spotService.getSpot(Mockito.anyString(), Mockito.same("bs1"), Mockito.anyString()))
				.thenReturn(ServiceResponse.ok(spot));

		Mockito.when(userTrackerService.hasTrackerInteractedWith(Mockito.any(), Mockito.contains("bc1")))
				.thenReturn(false);

		spotActionProcessor.processSpotClick("br1", new Tracker(null, null));

		Mockito.verify(contractPaymentService).issueContractPayment(Mockito.anyString(), Mockito.any());
	}

	@Test
	public void IfUserHasInteractedWithAdBeforePaymentShouldntHappen() {
		SpotData sd = new SpotData("cs1", "cr1", AdSource.CONTRACT);

		Mockito.when(urlRedirecterService.getSpotDataUsingRedirectId("cr1")).thenReturn(ServiceResponse.ok(sd));

		Spot spot = new Spot();
		spot.setContractId("cc1");
		Mockito.when(spotService.getSpot(Mockito.anyString(), Mockito.same("cs1"), Mockito.anyString()))
				.thenReturn(ServiceResponse.ok(spot));

		Mockito.when(userTrackerService.hasTrackerInteractedWith(Mockito.any(), Mockito.contains("cc1")))
				.thenReturn(true);

		spotActionProcessor.processSpotClick("cr1", new Tracker(null, null));

		Mockito.verify(contractPaymentService, Mockito.never()).issueContractPayment(Mockito.anyString(),
				Mockito.any());
	}

	@Test
	public void processSpotView_AdSourceNotContract_ShouldReturn() {
		AdModel am = new AdModel("");
		am.setAdSource(AdSource.SPOT_FALLBACK);
		Mockito.when(adModelCacheService.get("sd1")).thenReturn(am);

		spotActionProcessor.processSpotView("sd1", null);

		Mockito.verify(spotService, Mockito.never()).getSpot(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
	}

	@Test
	public void processSpotView_AdSourceIsContract_ShouldWork() {
		AdModel am = new AdModel("");
		am.setAdSource(AdSource.CONTRACT);
		Mockito.when(adModelCacheService.get("se1")).thenReturn(am);

		Spot s = new Spot();
		s.setContractId("ce1");
		Mockito.when(spotService.getSpot(Mockito.anyString(), Mockito.same("se1"), Mockito.anyString()))
				.thenReturn(ServiceResponse.ok(s));

		spotActionProcessor.processSpotView("se1", null);

		Mockito.verify(spotActionProcessor).verifyUserHasNotInteractedWithContract(Mockito.any(), Mockito.any(),
				Mockito.same("ce1"), Mockito.any(), Mockito.any());
	}
}
