package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.auth.AuthService;
import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.models.account.Balance;
import com.harystolho.adexchange.repositories.account.AccountRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.PasswordSecurity;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@InjectMocks
	private static AccountService accountService;

	@Mock
	AccountRepository authRepository;
	@Mock
	AuthService tokenService;
	@Mock
	Logger logger;

	@Test
	public void createAccountWithInvalidEmail() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "invalid.email",
				"some random password");
		assertEquals(ServiceResponseType.INVALID_EMAIL, response.getErrorType());
	}

	@Test
	public void createAccountWithValidEmail() {
		Mockito.when(authRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "valid2123@email.com",
				"some random password");
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "valid@email.com", "smal");
		assertEquals(ServiceResponseType.INVALID_PASSWORD, response.getErrorType());
	}

	@Test
	public void createAccountWithExistingEmail() {
		Mockito.when(authRepository.getByEmail("valid@email.com")).thenReturn(new Account("", ""));

		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "valid@email.com", "123456");
		assertEquals(ServiceResponseType.EMAIL_ALREADY_EXISTS, response.getErrorType());
	}

	@Test
	public void loginWithInvalidCredentials() {
		ServiceResponse<String> response = accountService.login("", "123456");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());

		ServiceResponse<String> response2 = accountService.login("valid@email.com", "");
		assertEquals(ServiceResponseType.FAIL, response2.getErrorType());
	}

	@Test
	public void loginWithNonExistentEmail() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<String> response = accountService.login("this@doesnt.exist", "123456");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithWrongPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = accountService.login("email@valid.com", "abc123");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithCorrectPassword() {
		Mockito.when(authRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = accountService.login("email@valid.com",
				PasswordSecurity.encryptPassword("123456"));
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void addBalanceToAccountShouldWork() {
		Account acc = new Account();
		acc.setBalance(new Balance("5.12"));
		Mockito.when(authRepository.getById("e864")).thenReturn(acc);

		ServiceResponseType response = accountService.addBalanceToAccount("e864", new Balance("7.00"));
		assertEquals(ServiceResponseType.OK, response);

		assertEquals(new Balance("12.12"), acc.getBalance());
	}

}
