package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.harystolho.adexchange.dao.AuthRepository;
import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.services.AuthService;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

public class AuthServiceTest {

	private static AuthService authService;
	private static AuthRepository authRepository;

	@BeforeClass
	public static void init() {
		authRepository = Mockito.mock(AuthRepository.class);

		authService = new AuthService(authRepository);
	}

	@Test
	public void createAccountWithInvalidEmail() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("invalid.email", "some random password");
		assertEquals(ServiceResponse.INVALID_EMAIL, response.getFist());
	}

	@Test
	public void createAccountWithValidEmail() {
		Mockito.when(authRepository.saveAccount(Mockito.any())).thenReturn(Pair.of(RepositoryResponse.CREATED, null));

		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid2@email.com", "some random password");
		assertEquals(ServiceResponse.OK, response.getFist());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "smal");
		assertEquals(ServiceResponse.INVALID_PASSWORD, response.getFist());
	}

	@Test
	public void createAccountWithExistingEmail() {
		Mockito.when(authRepository.getAccountByEmail("valid@email.com")).thenReturn(new Account("", ""));

		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "123456");
		assertEquals(ServiceResponse.EMAIL_ALREADY_EXISTS, response.getFist());
	}
}
