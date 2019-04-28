package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.auth.TokenService;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.RepositoryResponse;
import com.harystolho.adexchange.repositories.auth.AuthRepository;
import com.harystolho.adexchange.services.AuthService;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.utils.PasswordSecurity;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

	@InjectMocks
	private static AuthService authService;

	@Mock
	private static AuthRepository authRepository;
	@Mock
	private static TokenService tokenService;

	@Test
	public void createAccountWithInvalidEmail() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("invalid.email", "some random password");
		assertEquals(ServiceResponse.INVALID_EMAIL, response.getFist());
	}

	@Test
	public void createAccountWithValidEmail() {
		Mockito.when(authRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid2123@email.com",
				"some random password");
		assertEquals(ServiceResponse.OK, response.getFist());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		Pair<ServiceResponse, Nothing> response = authService.createAccount("valid@email.com", "smal");
		assertEquals(ServiceResponse.INVALID_PASSWORD, response.getFist());
	}

	@Test
	public void createAccountWithExistingEmail() {
		Mockito.when(authRepository.getByEmail("valid@email.com")).thenReturn(new Account("", ""));

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
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		Pair<ServiceResponse, String> response = authService.login("this@doesnt.exist", "123456");
		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void loginWithWrongPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		Pair<ServiceResponse, String> response = authService.login("email@valid.com", "abc123");
		assertEquals(ServiceResponse.FAIL, response.getFist());
	}

	@Test
	public void loginWithCorrectPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		Pair<ServiceResponse, String> response = authService.login("email@valid.com",
				PasswordSecurity.encryptPassword("123456"));
		assertEquals(ServiceResponse.OK, response.getFist());
	}

}
