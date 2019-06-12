package com.harystolho.adexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.harystolho.adexchange.payment.pagseguro.PaymentConfiguration;

@Configuration
public class PaymentConfig {

	@Bean
	public PaymentConfiguration paymentConfiguration(@Value("${payment.authentication.email}") String email,
			@Value("${payment.authentication.token}") String token,
			@Value("${payment.api.endpoint}") String apiEndpoint) {
		return new PaymentConfiguration(email, token, apiEndpoint);
	}

}
