package com.harystolho.adServer.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.ContractService;
import com.harystolho.adexchange.services.ServiceResponse;

@RunWith(MockitoJUnitRunner.class)
public class ContractPaymentServiceTest {

	@InjectMocks
	ContractPaymentService contractPaymentService;

	@Mock
	ContractService contractService;
	@Mock
	AccountService accountService;
	@Mock
	Logger logger;

	@Test
	public void issuePaymentToInvalidContractShouldLogError() {
		Mockito.when(contractService.getContractById(Mockito.any(), Mockito.same("abcd")))
				.thenReturn(ServiceResponse.fail(""));

		contractPaymentService.issueContractPayment("abcd");

		Mockito.verify(logger).error(Mockito.any(), Mockito.same("abcd"));
	}

	@Test
	public void issuePaymentToValidContractShouldWork() {
		Mockito.when(contractService.getContractById(Mockito.any(), Mockito.same("klm"))).thenAnswer((inv) -> {
			Contract contract = new Contract();
			contract.setPaymentMethod(PaymentMethod.PAY_PER_CLICK);
			contract.setCreatorId("123");
			contract.setAcceptorId("456");
			contract.setPaymentValue("5,00");

			return ServiceResponse.ok(contract);
		});

		contractPaymentService.issueContractPayment("klm");

		Mockito.verify(accountService).transferBalance(Mockito.contains("123"), Mockito.contains("456"),
				Mockito.contains("5.00"));
	}

	
	
}
