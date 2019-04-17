package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.harystolho.adexchange.services.AuthService;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

public class AuthServiceTest {

	private static AuthService authService;

	@BeforeClass
	public static void init() {
		authService = new AuthService();
	}

	@Test
	public void createAccountWithInvalidEmail() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("invalid.email", "some random password");
		assertEquals(ServiceResponse.INVALID_EMAIL, response.getFist());
	}

	@Test
	public void createAccountWithValidEmail() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "some random password");
		assertEquals(ServiceResponse.OK, response.getFist());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "smal");
		assertEquals(ServiceResponse.INVALID_PASSWORD, response.getFist());
	}
	
	@Test
	public void createAccountWithExistingEmail() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "123456");
		
	}
}
