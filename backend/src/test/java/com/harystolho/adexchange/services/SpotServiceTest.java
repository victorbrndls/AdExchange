package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.repositories.spot.SpotRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class SpotServiceTest {

	private static final String VALID_NAME = "Spot Valid Name";

	@InjectMocks
	SpotService spotService;

	@Mock
	SpotRepository spotRepository;

	@Mock
	ContractService contractService;

	@Mock
	AdService adService;

	@Mock
	EventDispatcher eventDispatcher;

	@Test
	public void createSpotWith_InvalidName() {
		ServiceResponse<Spot> response = spotService.createOrUpdateSpot("abc", null, "", "", "");

		assertEquals(ServiceResponseType.INVALID_SPOT_NAME, response.getErrorType());
	}

	@Test
	public void updateSpot() {
		Spot spot = new Spot();
		spot.setAccountId("123");
		Mockito.when(spotRepository.getById(Mockito.same("spot1"))).thenReturn(spot);
		Mockito.when(spotRepository.save(Mockito.any())).then((inv) -> {
			return inv.getArgument(0);
		});

		ServiceResponse<Spot> response = spotService.createOrUpdateSpot("123", "spot1", "New Name", "c1", "ad1");

		Mockito.verify(eventDispatcher).dispatch(Mockito.any());

		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void UpdateSpotThatDoesntBelongToUser() {
		Spot spot = new Spot();
		spot.setAccountId("a15");
		Mockito.when(spotRepository.getById(Mockito.same("spot2"))).thenReturn(spot);

		ServiceResponse<Spot> response = spotService.createOrUpdateSpot("a12", "spot2", "New Name", "", "");

		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

	@Test
	public void getSpotWithInvalidId() {
		ServiceResponse<Spot> response = spotService.getSpot("ac8", "invalid", "");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void getSpotThatDoesntBelongToUser() {
		Spot spot = new Spot();
		spot.setAccountId("a90");
		Mockito.when(spotRepository.getById(Mockito.same("spot90"))).thenReturn(spot);

		ServiceResponse<Spot> response = spotService.getSpot("ac0", "spot90", "");

		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

	@Test
	public void getSpotWithEmbedContract() {
		Spot spot = new Spot();
		spot.setAccountId("a91");
		spot.setContractId("c91");
		Mockito.when(spotRepository.getById(Mockito.same("spot91"))).thenReturn(spot);

		Mockito.when(contractService.getContractById(Mockito.same("a91"), Mockito.same("c91")))
				.thenReturn(ServiceResponse.ok(new Contract()));

		ServiceResponse<Spot> response = spotService.getSpot("a91", "spot91", "contract");

		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}
}
