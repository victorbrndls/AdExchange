package com.harystolho.adServer.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adServer.templates.AdTemplateService;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.account.Balance;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.SpotService;

@RunWith(MockitoJUnitRunner.class)
public class AdModelServiceTest {

	@InjectMocks
	static AdModelFactory adModelService;

	@Mock
	SpotService spotService;
	@Mock
	AdService adService;
	@Mock
	UrlRedirecterService urlRedirecterService;
	@Mock
	AdTemplateService adTemplateService;
	@Mock
	AccountService accountService;
	@Mock
	AdModelService adServerService;

	@Test
	public void removeSpotsThatTheUserCantPayFor() {
		Mockito.when(accountService.hasAccountBalance(Mockito.contains("rst1"), Mockito.any())).thenReturn(false);

		Spot s1 = new Spot();
		s1.setContractId("rst_c1");

		Spot s2 = new Spot();
		s2.setContractId("rst_c1");

		Contract c1 = new Contract();
		c1.setId("rst_c1");
		c1.setCreatorId("rst1");
		c1.setPaymentValue("7,00");

		adModelService.updateDataCacheEntry(s1, c1);
		adModelService.updateDataCacheEntry(s2, c1);

		adModelService.updateSpotsAdvertisedByUser("rst1");

		Mockito.verify(adServerService).updateSpot(s1);
		Mockito.verify(adServerService).updateSpot(s2);
	}

	@Test
	public void removeSpotsThatTheUserCantPayForButKeepOthers() {
		Mockito.when(accountService.hasAccountBalance(Mockito.contains("rst2"), Mockito.anyString()))
				.thenAnswer((inv) -> {
					Balance b = new Balance((String) inv.getArgument(1));

					return b.canSubtract(new Balance("5.00"));
				});

		Spot s1 = new Spot();
		s1.setContractId("rst_c1");
		Spot s2 = new Spot();
		s2.setContractId("rst_c2");

		Contract c1 = new Contract();
		c1.setId("rst_c1");
		c1.setCreatorId("rst2");
		c1.setPaymentValue("7,00");

		Contract c2 = new Contract();
		c2.setId("rst_c2");
		c2.setCreatorId("rst2");
		c2.setPaymentValue("4,00");

		adModelService.updateDataCacheEntry(s1, c1);
		adModelService.updateDataCacheEntry(s2, c2);

		adModelService.updateSpotsAdvertisedByUser("rst2");

		Mockito.verify(adServerService, Mockito.never()).updateSpot(s1);
		Mockito.verify(adServerService).updateSpot(s2);
	}

}
