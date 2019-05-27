package com.harystolho.adexchange.services.payment;

import java.util.Arrays;

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
import com.harystolho.adexchange.services.payment.ContractPaymentService;

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

		contractPaymentService.issueContractPayment("abcd", Arrays.asList());

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

		contractPaymentService.issueContractPayment("klm", Arrays.asList(PaymentMethod.PAY_PER_CLICK));

		Mockito.verify(accountService).transferBalance(Mockito.contains("123"), Mockito.contains("456"),
				Mockito.contains("5.00"));
	}

	@Test
	public void issuePaymentToContractWithPaymentMethodNotAllowed_ShoudFail() {
		Mockito.when(contractService.getContractById(Mockito.any(), Mockito.same("cc1"))).thenAnswer((inv) -> {
			Contract contract = new Contract();
			contract.setPaymentMethod(PaymentMethod.PAY_ONCE);
			contract.setCreatorId("ac1");
			contract.setAcceptorId("ac2");
			contract.setPaymentValue("5,00");

			return ServiceResponse.ok(contract);
		});

		contractPaymentService.issueContractPayment("cc1", Arrays.asList(PaymentMethod.PAY_PER_CLICK));

		Mockito.verify(accountService, Mockito.never()).transferBalance(Mockito.any(), Mockito.any(), Mockito.any());
	}

}
