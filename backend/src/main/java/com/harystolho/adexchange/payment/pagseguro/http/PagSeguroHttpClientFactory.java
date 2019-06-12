package com.harystolho.adexchange.payment.pagseguro.http;

import java.util.HashMap;
import java.util.Map;

public class PagSeguroHttpClientFactory {

	public PagSeguroHttpClient createClient(String authenticationEmail, String authenticationToken) {
		PagSeguroHttpClient httpClient = new PagSeguroHttpClient();

		addDefaultHeaders(httpClient, authenticationEmail, authenticationToken);

		return httpClient;
	}

	private void addDefaultHeaders(PagSeguroHttpClient httpClient, String authenticationEmail,
			String authenticationToken) {
		Map<String, String> params = new HashMap<>();

		params.put("email", authenticationEmail);
		params.put("token", authenticationToken);

		params.put("currency", "BRL");
		params.put("shippingAddressRequired", "false");
		
		httpClient.addBodyParams(params);
	}

}
