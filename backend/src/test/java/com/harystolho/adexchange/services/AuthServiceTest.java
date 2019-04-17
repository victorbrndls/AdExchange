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
import com.harystolho.adexchange.utils.PasswordSecurity;

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
		Mockito.when(authRepository.getAccountByEmail(Mockito.anyString())).thenReturn(null);

		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid2123@email.com", "some random password");
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

	@Test
	public void loginWithInvalidCredentials() {
		Pair<ServiceResponse, String> response = authService.login("", "123456");
		assertEquals(ServiceResponse.FAIL, response.getFist());

		Pair<ServiceResponse, String> response2 = authService.login("valid@email.com", "");
		assertEquals(ServiceResponse.FAIL, response2.getFist());
	}

	@Test
	public void loginWithNonExistentEmail() {
		Mockito.when(authRepository.getAccountByEmail(Mockito.anyString())).thenReturn(null);

		Pair<ServiceResponse, String> response = authService.login("this@doesnt.exist", "123456");
		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void loginWithWrongPassword() {
		Mockito.when(authRepository.getAccountByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		Pair<ServiceResponse, String> response = authService.login("email@valid.com", "abc123");
		assertEquals(ServiceResponse.FAIL, response.getFist());
	}
	
	@Test
	public void loginWithCorrectPassword() {
		Mockito.when(authRepository.getAccountByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		Pair<ServiceResponse, String> response = authService.login("email@valid.com", PasswordSecurity.encryptPassword("123456"));
		assertEquals(ServiceResponse.OK, response.getFist());
	}
	
}
