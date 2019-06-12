package com.harystolho.adexchange.payment.pagseguro;

/**
 * Contains information needed to use the PagSeguro's API.
 * 
 * @author Harystolho
 *
 */
public class PaymentConfiguration {

	private final String email;
	private final String token;
	private final String endpoint;

	public PaymentConfiguration(String email, String token, String endpoint) {
		this.email = email;
		this.token = token;
		this.endpoint = endpoint;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public String getEndpoint() {
		return endpoint;
	}

}
