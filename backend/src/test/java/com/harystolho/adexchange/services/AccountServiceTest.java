package com.harystolho.adexchange.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.auth.AuthService;
import com.harystolho.adexchange.events.EventDispatcher;
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
	AccountRepository accountRepository;
	@Mock
	AuthService tokenService;
	@Mock
	Logger logger;
	@Mock
	EventDispatcher eventDispatcher;

	@Test
	public void createAccountWithInvalidName() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "not", "valid@email.com",
				"some random password");
		assertEquals(ServiceResponseType.INVALID_ACCOUNT_NAME, response.getErrorType());
	}

	@Test
	public void createAccountWithInvalidEmptyName_ShouldFail() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "        ", "valid@email.com",
				"some random password");
		assertEquals(ServiceResponseType.INVALID_ACCOUNT_NAME, response.getErrorType());
	}

	@Test
	public void createAccountWithInvalidEmail() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "Valid name", "invalid.email",
				"some random password");
		assertEquals(ServiceResponseType.INVALID_EMAIL, response.getErrorType());
	}

	@Test
	public void createAccountWithValidEmail() {
		Mockito.when(accountRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(accountRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "Vialid name",
				"valid2123@email.com", "some random password");
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void createAccountWithInvalidPassword() {
		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "valid name", "valid@email.com",
				"smal");
		assertEquals(ServiceResponseType.INVALID_PASSWORD, response.getErrorType());
	}

	@Test
	public void createAccountWithExistingEmail() {
		Mockito.when(accountRepository.getByEmail("valid@email.com")).thenReturn(new Account("", ""));

		ServiceResponse<Account> response = accountService.createOrUpdateAccount(null, "valid name", "valid@email.com",
				"123456");
		assertEquals(ServiceResponseType.EMAIL_ALREADY_EXISTS, response.getErrorType());
	}

	@Test
	public void updateAccountWithInvalidName_ShouldFail() {
		Mockito.when(accountRepository.getById("a76")).thenReturn(new Account());

		ServiceResponse<Account> response = accountService.updateAccountInfo("a76", "dn        ");
		assertEquals(ServiceResponseType.INVALID_ACCOUNT_NAME, response.getErrorType());
	}

	@Test
	public void updateAccountWithValidName_ShouldWork() {
		Account account = new Account();
		Mockito.when(accountRepository.getById("a85")).thenReturn(account);

		ServiceResponse<Account> response = accountService.updateAccountInfo("a85", "New Long Name");

		assertEquals(ServiceResponseType.OK, response.getErrorType());
		assertEquals("New Long Name", account.getFullName());
	}

	@Test
	public void updateAccountWithValidEmail_ShouldWork() {
		Account account = new Account();
		Mockito.when(accountRepository.getById("a105")).thenReturn(account);

		ServiceResponse<Account> response = accountService.updateAccountAuth("a105", "newemail@mail.com", "MyPassword");

		assertEquals(ServiceResponseType.OK, response.getErrorType());

		assertEquals("newemail@mail.com", account.getEmail());
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
		Mockito.when(accountRepository.getByEmail(Mockito.anyString())).thenReturn(null);

		ServiceResponse<String> response = accountService.login("this@doesnt.exist", "123456");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithWrongPassword() {
		Mockito.when(accountRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = accountService.login("email@valid.com", "abc123");
		assertEquals(ServiceResponseType.FAIL, response.getErrorType());
	}

	@Test
	public void loginWithCorrectPassword() {
		Mockito.when(accountRepository.getByEmail(Mockito.anyString()))
				.thenReturn(new Account("email@valid.com", PasswordSecurity.encryptPassword("123456")));

		ServiceResponse<String> response = accountService.login("email@valid.com", "123456");
		assertEquals(ServiceResponseType.OK, response.getErrorType());
	}

	@Test
	public void addBalanceToAccountShouldWork() {
		Account acc = new Account();
		acc.setBalance(new Balance("5.12"));
		Mockito.when(accountRepository.getById("e864")).thenReturn(acc);

		ServiceResponseType response = accountService.addBalanceToAccount("e864", new Balance("7.00"));
		assertEquals(ServiceResponseType.OK, response);

		assertEquals(new Balance("12.12"), acc.getBalance());
	}

	@Test
	public void subtractBalanceFromAccountShouldWork() {
		Account acc = new Account();
		acc.setBalance(new Balance("15.00"));
		Mockito.when(accountRepository.getById("o78")).thenReturn(acc);

		ServiceResponseType response = accountService.subtractBalanceFromAccount("o78", new Balance("6.00"));
		assertEquals(ServiceResponseType.OK, response);

		assertEquals(new Balance("9.00"), acc.getBalance());
	}

	@Test
	public void subtractBalanceFromAccountThatDoesntHaveEnoughShouldFail() {
		Account acc = new Account();
		acc.setBalance(new Balance("4.00"));
		Mockito.when(accountRepository.getById("l79")).thenReturn(acc);

		ServiceResponseType response = accountService.subtractBalanceFromAccount("l79", new Balance("6.00"));
		assertEquals(ServiceResponseType.INSUFFICIENT_ACCOUNT_BALANCE, response);

		assertEquals(new Balance("4.00"), acc.getBalance());
	}

	@Test
	public void hasAccountBalance_ShouldWork() {
		Account acc = new Account();
		acc.setBalance(new Balance("14.51"));
		Mockito.when(accountRepository.getById("a187")).thenReturn(acc);

		assertTrue(accountService.hasAccountBalance("a187", "14.00"));
	}

	@Test
	public void hasAccountBalanceWithAccountThatDoesntHaveBalance_ShouldFail() {
		Account acc = new Account();
		acc.setBalance(new Balance("3.00"));
		Mockito.when(accountRepository.getById("a198")).thenReturn(acc);

		assertFalse(accountService.hasAccountBalance("a198", "4.00"));
	}

	@Test
	public void transferBalanceShouldWork() {
		Account a1 = new Account();
		a1.setId("09_1");
		a1.setBalance(new Balance("10.00"));
		Mockito.when(accountRepository.getById("09_1")).thenReturn(a1);

		Account a2 = new Account();
		a2.setBalance(new Balance("1.00"));
		a2.setId("09_2");
		Mockito.when(accountRepository.getById("09_2")).thenReturn(a2);

		accountService.transferBalance("09_1", "09_2", "3.00");

		Mockito.verify(logger, Mockito.never()).error(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());

		assertEquals(new Balance("7.00"), a1.getBalance());
		assertEquals(new Balance("4.00"), a2.getBalance());
	}

	@Test
	public void transferBalanceFromInvalidAccount() {
		Mockito.when(accountRepository.getById("03=51")).thenReturn(null);

		accountService.transferBalance("03=51", "123", "1.00");

		Mockito.verify(logger).error(Mockito.anyString(), Mockito.contains("03=51"), Mockito.anyString(),
				Mockito.anyString());
	}

	@Test
	public void transferBalanceToInvalidAccount() {
		Mockito.when(accountRepository.getById("06_1")).thenReturn(new Account());
		Mockito.when(accountRepository.getById("06_2")).thenReturn(null);

		accountService.transferBalance("06_1", "06_2", "3.00");

		Mockito.verify(logger).error(Mockito.anyString(), Mockito.contains("06_1"), Mockito.contains("06_2"),
				Mockito.anyString());
	}

	@Test
	public void transferBalanceWithInvalidBalance() {
		Mockito.when(accountRepository.getById("06_1")).thenReturn(new Account());
		Mockito.when(accountRepository.getById("06_2")).thenReturn(new Account());

		accountService.transferBalance("06_1", "06_2", "12,00");

		Mockito.verify(logger).error(Mockito.anyString(), Mockito.contains("06_1"), Mockito.contains("06_2"),
				Mockito.contains("12,00"));
	}

	@Test
	public void transferBalanceFromAccountThatDoesntHaveEnoughBalance() {
		Account a1 = new Account();
		a1.setId("96_1");
		a1.setBalance(new Balance("10.00"));

		Mockito.when(accountRepository.getById("96_1")).thenReturn(a1);
		Mockito.when(accountRepository.getById("96_2")).thenReturn(new Account());

		accountService.transferBalance("96_1", "96_2", "14.00");

		Mockito.verify(logger).error(Mockito.anyString(), Mockito.contains("96_1"), Mockito.any(),
				Mockito.contains("14.00"));
	}
}
