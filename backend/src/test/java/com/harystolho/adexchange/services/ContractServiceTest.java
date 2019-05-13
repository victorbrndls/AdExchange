package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.repositories.contract.ContractRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class ContractServiceTest {

	@InjectMocks
	ContractService contractService;

	@Mock
	ContractRepository contractRepository;

	@Mock
	AdService adService;

	@Mock
	WebsiteService websiteService;

	@Test
	public void deleteContract_shouldWork() {
		Contract contract = new Contract();
		contract.setCreatorId("ac1");
		contract.setId("123");
		contract.setExpiration(LocalDateTime.now().minusDays(1));

		Mockito.when(contractRepository.getById("123")).thenReturn(contract);

		ServiceResponse<Contract> response = contractService.deleteContractForUser("ac1", "123");
		assertEquals(ServiceResponseType.OK, response.getErrorType());
		assertEquals(contract.getCreatorId(), "");
	}

	@Test
	public void deleteContract_ThatDoesntBelongToUser_ShouldFail() {
		Contract contract = new Contract();
		contract.setCreatorId("ab2");
		contract.setId("333");
		contract.setExpiration(LocalDateTime.now().minusDays(1));

		Mockito.when(contractRepository.getById("333")).thenReturn(contract);

		ServiceResponse<Contract> response = contractService.deleteContractForUser("aki", "333");
		assertEquals(ServiceResponseType.UNAUTHORIZED, response.getErrorType());
	}

	@Test
	public void deleteContract_ThatHasNotExpired_ShouldFail() {
		Contract contract = new Contract();
		contract.setCreatorId("ak1");
		contract.setId("123");
		contract.setExpiration(LocalDateTime.now().plusDays(1));

		Mockito.when(contractRepository.getById("123")).thenReturn(contract);

		ServiceResponse<Contract> response = contractService.deleteContractForUser("ak1", "123");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void deleteContract_RemoveFromDatabase_ShouldWork() {
		Contract contract = new Contract();
		contract.setCreatorId("ai78");
		contract.setAcceptorId("");
		contract.setId("456");
		contract.setExpiration(LocalDateTime.now().minusDays(1));

		Mockito.when(contractRepository.getById("456")).thenReturn(contract);

		ServiceResponse<Contract> response = contractService.deleteContractForUser("ai78", "456");
		Mockito.verify(contractRepository, Mockito.times(0)).save(Mockito.any());
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}
}
