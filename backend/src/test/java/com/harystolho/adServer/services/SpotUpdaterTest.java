package com.harystolho.adserver.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adserver.data.AdModelData;
import com.harystolho.adserver.data.AdModelDataCache;
import com.harystolho.adserver.services.admodel.AdModelService;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.account.Balance;
import com.harystolho.adexchange.services.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class SpotUpdaterTest {

	@InjectMocks
	SpotUpdater spotUpdater;

	@Mock
	AdModelDataCache adModelCache;
	@Mock
	AccountService accountService;
	@Mock
	AdModelService adModelService;

	@Test
	public void removeSpotsThatTheUserCantPayFor() {
		Spot s1 = new Spot();
		s1.setContractId("asc1");

		Spot s2 = new Spot();
		s2.setContractId("asc1");

		Contract c1 = new Contract();
		c1.setId("asc1");
		c1.setCreatorId("aa1");
		c1.setPaymentValue("7,00");

		AdModelData amd = new AdModelData();
		amd.addContract(c1);
		amd.addSpot(s1);
		amd.addSpot(s2);

		Mockito.when(adModelCache.get("aa1")).thenReturn(amd);

		Mockito.when(accountService.hasAccountBalance(Mockito.contains("aa1"), Mockito.any())).thenReturn(false);

		spotUpdater.updateSpotsAdvertisedByUser("aa1");

		Mockito.verify(adModelService).updateSpot(s1);
		Mockito.verify(adModelService).updateSpot(s2);
	}

	@Test
	public void removeSpotsThatTheUserCantPayForButKeepOthers() {
		Mockito.when(accountService.hasAccountBalance(Mockito.contains("ba1"), Mockito.anyString()))
				.thenAnswer((inv) -> {
					Balance b = new Balance((String) inv.getArgument(1));

					return b.canSubtract(new Balance("5.00"));
				});

		Spot s1 = new Spot();
		s1.setContractId("bc1");
		Spot s2 = new Spot();
		s2.setContractId("bc2");

		Contract c1 = new Contract();
		c1.setId("bc1");
		c1.setCreatorId("ba1");
		c1.setPaymentValue("7,00");

		Contract c2 = new Contract();
		c2.setId("bc2");
		c2.setCreatorId("ba1");
		c2.setPaymentValue("4,00");

		AdModelData amd = new AdModelData();
		amd.addContract(c1);
		amd.addContract(c2);
		amd.addSpot(s1);
		amd.addSpot(s2);

		Mockito.when(adModelCache.get("ba1")).thenReturn(amd);

		spotUpdater.updateSpotsAdvertisedByUser("ba1");

		Mockito.verify(adModelService, Mockito.never()).updateSpot(s1);
		Mockito.verify(adModelService).updateSpot(s2);
	}

}
