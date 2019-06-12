package com.harystolho.adexchange.payment.pagseguro;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.payment.pagseguro.PaymentCheckoutGenerator;
import com.harystolho.adexchange.payment.pagseguro.PaymentProduct.PaymentProductType;
import com.harystolho.adexchange.payment.pagseguro.ProductRegistry;
import com.harystolho.adexchange.payment.pagseguro.http.PagSeguroHttpClient;
import com.harystolho.adexchange.payment.pagseguro.http.PagSeguroHttpClientFactory;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Pair;

@RunWith(MockitoJUnitRunner.class)
public class PaymentCheckoutGeneratorTest {

	@InjectMocks
	private PaymentCheckoutGenerator checkoutGenerator;

	@Mock
	private ProductRegistry productRegistry;
	@Mock
	private PaymentConfiguration paymentConfiguration;
	@Mock
	private PagSeguroHttpClientFactory factory;

	@Test
	public void generateCheckoutForInvalidProduct_ShouldFail() {
		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(null);

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void generateCheckout_ShouldWork() {
		Mockito.when(paymentConfiguration.getEmail()).thenReturn("a@a.com");
		Mockito.when(paymentConfiguration.getToken()).thenReturn("123");

		Mockito.when(productRegistry.getProduct(PaymentProductType.BALANCE_50))
				.thenReturn(new PaymentProduct.PaymentProductBuilder().id("01").description("some desc").amount("9.90")
						.quantity("1").weight("0").build());

		PagSeguroHttpClient httpClientMock = Mockito.mock(PagSeguroHttpClient.class);
		Mockito.when(httpClientMock.connect())
				.thenReturn(Pair.of(ServiceResponseType.OK, "<checkout><code>123checkout</code></checkout>"));

		Mockito.when(factory.createClient(Mockito.anyString(), Mockito.anyString())).thenReturn(httpClientMock);

		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(PaymentProductType.BALANCE_50);

		assertEquals(ServiceResponseType.OK, response.getErrorType());
		assertEquals("123checkout", response.getReponse());
	}

	@Test
	public void generateCheckoutWithApiError_ShouldFail() {
		Mockito.when(paymentConfiguration.getEmail()).thenReturn("a@a.com");
		Mockito.when(paymentConfiguration.getToken()).thenReturn("123");

		Mockito.when(productRegistry.getProduct(PaymentProductType.BALANCE_50))
				.thenReturn(new PaymentProduct.PaymentProductBuilder().id("01").description("some desc").amount("9.90")
						.quantity("1").weight("0").build());

		PagSeguroHttpClient httpClientMock = Mockito.mock(PagSeguroHttpClient.class);
		Mockito.when(httpClientMock.connect()).thenReturn(Pair.of(ServiceResponseType.FAIL, null));

		Mockito.when(factory.createClient(Mockito.anyString(), Mockito.anyString())).thenReturn(httpClientMock);

		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(PaymentProductType.BALANCE_50);

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void generateCheckoutWithInvalidResponse_ShouldFail() {
		Mockito.when(paymentConfiguration.getEmail()).thenReturn("a@a.com");
		Mockito.when(paymentConfiguration.getToken()).thenReturn("123");

		Mockito.when(productRegistry.getProduct(PaymentProductType.BALANCE_25))
				.thenReturn(new PaymentProduct.PaymentProductBuilder().id("02").description("some desc").amount("9.90")
						.quantity("1").weight("0").build());

		PagSeguroHttpClient httpClientMock = Mockito.mock(PagSeguroHttpClient.class);
		Mockito.when(httpClientMock.connect()).thenReturn(Pair.of(ServiceResponseType.OK, "not_valid_xml"));

		Mockito.when(factory.createClient(Mockito.anyString(), Mockito.anyString())).thenReturn(httpClientMock);

		ServiceResponse<String> response = checkoutGenerator.generateCheckoutCode(PaymentProductType.BALANCE_25);

		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}
}
