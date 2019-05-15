package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.auth.TokenService;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.account.AccountRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.PasswordSecurity;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

	@InjectMocks
	private static AuthService authService;

	@Mock
	private static AccountRepository authRepository;
	@Mock
	private static TokenService tokenService;

	@Test
	public void createAccountWithInvalidEmail() {
		ServiceResponse<Nothing> response = authService.createAccount("invalid.email", "some random password");
		assertEquals(ServiceResponseType.INVALID_EMAIL, response.getErrorType());
	}

	@Test
	public void createAccountWithValidEmail() {
		Mockito.when(authRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<Nothing> response = authService.createAccount("valid2123@email.com", "some random password");
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		ServiceResponse<Nothing> response = authService.createAccount("valid@email.com", "smal");
		assertEquals(ServiceResponseType.INVALID_PASSWORD, response.getErrorType());
	}

	@Test
	public void createAccountWithExistingEmail() {
		Mockito.when(authRepository.getByEmail("valid@email.com")).thenReturn(new Account("", ""));

		ServiceResponse<Nothing> response = authService.createAccount("valid@email.com", "123456");
		assertEquals(ServiceResponseType.EMAIL_ALREADY_EXISTS, response.getErrorType());
	}

	@Test
	public void loginWithInvalidCredentials() {
		ServiceResponse<String> response = authService.login("", "123456");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());

		ServiceResponse<String> response2 = authService.login("valid@email.com", "");
		assertEquals(ServiceResponseType.FAIL, response2.getErrorType());
	}

	@Test
	public void loginWithNonExistentEmail() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<String> response = authService.login("this@doesnt.exist", "123456");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithWrongPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = authService.login("email@valid.com", "abc123");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithCorrectPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = authService.login("email@valid.com",
				PasswordSecurity.encryptPassword("123456"));
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

}
