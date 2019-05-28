package com.harystolho.adexchange.services.payment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.events.spots.events.SpotClickedEvent;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.services.AdModelFactory.AdSource;
import com.harystolho.adserver.services.UrlRedirecterService.SpotData;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;

@RunWith(MockitoJUnitRunner.class)
public class ContractPaymentVerifierTest {

	@InjectMocks
	SpotActionVerifier contractPaymentVerifier;

	@Mock
	UrlRedirecterService urlRedirecterService;
	@Mock
	ContractPaymentService contractPaymentService;
	@Mock
	SpotService spotService;
	@Mock
	UserTrackerService userTrackerService;

	@Test
	public void AdCreatedFromSpotFallbackShouldntBeBilled() {
		SpotData sd = new SpotData("as1", "ar1", AdSource.SPOT_FALLBACK);

		Mockito.when(urlRedirecterService.getSpotDataUsingRedirectId("ar1")).thenReturn(ServiceResponse.ok(sd));

		contractPaymentVerifier.verifySpotClick("ar1", new Tracker(null, null));

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

		contractPaymentVerifier.verifySpotClick("br1", new Tracker(null, null));

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

		contractPaymentVerifier.verifySpotClick("cr1", new Tracker(null, null));

		Mockito.verify(contractPaymentService, Mockito.never()).issueContractPayment(Mockito.anyString(),
				Mockito.any());
	}

}
