package com.harystolho.adexchange.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AEUtils {

	public static final String ADMIN_ACESS_ID = "ADMIN";

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
