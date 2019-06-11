package com.harystolho.adexchange.payment.pagseguro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.payment.pagseguro.PaymentCheckoutGenerator;
import com.harystolho.adexchange.payment.pagseguro.ProductRegistry;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@RunWith(MockitoJUnitRunner.class)
public class PaymentCheckoutGeneratorTest {

	@InjectMocks
	private PaymentCheckoutGenerator checkoutGenerator;

	@Mock
	private ProductRegistry productRegistry;
	@Mock
	private PaymentConfiguration paymentConfiguration;

	@Test
	public void generateCheckoutWithInvalidApiEndpoint_ShouldFail() {
		Mockito.when(paymentConfiguration.getEndpoint()).thenReturn("invalid://sandbox.pagseguro.uol.com.br");

		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(null);

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void generateCheckoutForInvalidProduct_ShouldFail() {
		Mockito.when(paymentConfiguration.getEndpoint()).thenReturn("https://sandbox.pagseguro.uol.com.br");

		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(null);

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}
}
