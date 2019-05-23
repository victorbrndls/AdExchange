package com.harystolho.adexchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.auth.AuthService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.account.AccountBalanceChangedEvent;
import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.models.account.Balance;
import com.harystolho.adexchange.models.account.Balance.BalanceException;
import com.harystolho.adexchange.repositories.account.AccountRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.PasswordSecurity;

@Service
public class AccountService {

	private AccountRepository accountRepository;
	private AuthService tokenService;
	private Logger logger;
	private EventDispatcher eventDispatcher;

	@Autowired
	public AccountService(@Qualifier("cachedAccountRepository") AccountRepository accountRepository,
			AuthService tokenService, Logger logger, EventDispatcher eventDispatcher) {
		this.accountRepository = accountRepository;
		this.tokenService = tokenService;
		this.logger = logger;
		this.eventDispatcher = eventDispatcher;
	}

	/**
	 * Creates a new {@link Account} if the {accountId} is <code>null</code>.
	 * Updates an existing account if the {accountId} is not <code>null</code>
	 * 
	 * @param accountId
	 * @param email
	 * @param password
	 * @return
	 */
	public ServiceResponse<Account> createOrUpdateAccount(String accountId, String email, String password) {
		email = sanitizeEmail(email);

		if (!verifyEmail(email))
			return ServiceResponse.error(ServiceResponseType.INVALID_EMAIL);

		if (!verifyPassword(password))
			return ServiceResponse.error(ServiceResponseType.INVALID_PASSWORD);

		if (emailExists(email))
			return ServiceResponse.error(ServiceResponseType.EMAIL_ALREADY_EXISTS);

		Account acc = accountRepository.getById(accountId); // Get account to update

		if (acc == null) // If account is null
			acc = new Account(); // Create a new account

		acc.setEmail(email);
		acc.setPassword(PasswordSecurity.encryptPassword(password));

		return ServiceResponse.ok(accountRepository.save(acc));
	}

	public ServiceResponse<String> login(String email, String password) {
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
			return ServiceResponse.fail("Email or/and password can't be blank");

		Account possibleAccount = accountRepository.getByEmail(sanitizeEmail(email));

		if (possibleAccount == null) {
			return ServiceResponse.fail(null);
		}

		if (!PasswordSecurity.comparePasswords(possibleAccount.getPassword(),
				PasswordSecurity.encryptPassword(password))) {
			return ServiceResponse.fail(null);
		}

		String token = tokenService.generateTokenForAccount(possibleAccount.getId());

		return ServiceResponse.ok(token);
	}

	/**
	 * Updates the account name,
	 * 
	 * @param accountId
	 * @param name
	 * @return
	 */
	public ServiceResponse<Account> updateAccountInfo(String accountId, String name) {
		Account acc = accountRepository.getById(accountId);

		if (acc == null)
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_ID);

		if (!StringUtils.hasText(name)) // TODO limit name size
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_NAME);

		acc.setFullName(name);
		accountRepository.save(acc);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Account> getAccountById(String accountId) {
		Account account = accountRepository.getById(accountId);

		if (account != null) {
			return ServiceResponse.ok(account);
		} else {
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_ID);
		}
	}

	/**
	 * Standardizes the email. Make it lower case so it's easier to find accounts by
	 * email on the database
	 * 
	 * @param email
	 * @return
	 */
	private String sanitizeEmail(String email) {
		return email.trim().toLowerCase();
	}

	/**
	 * @param email
	 * @return true if the email is valid
	 */
	private boolean verifyEmail(String email) {
		return email.matches("([\\w.]+@[\\w.]+)");
	}

	/**
	 * @param email
	 * @return true if the password is valid
	 */
	private boolean verifyPassword(String password) {
		return password.length() >= 5;
	}

	private boolean emailExists(String email) {
		return accountRepository.getByEmail(email) != null;
	}

	/**
	 * @param accountId
	 * @return the {@link Account#fullName} or <code>null</code>
	 */
	public String getAccountNameById(String accountId) {
		Account acc = accountRepository.getById(accountId);

		if (acc != null)
			return acc.getFullName();

		return null;
	}

	public ServiceResponse<String> getAccountBalance(String accountId) {
		Account acc = accountRepository.getById(accountId);

		if (acc == null)
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_ID);

		return ServiceResponse.ok(acc.getBalance().toString());
	}

	public ServiceResponseType addBalanceToAccount(String accountId, Balance balance) {
		Account acc = accountRepository.getById(accountId);

		if (acc == null)
			return ServiceResponseType.INVALID_ACCOUNT_ID;

		Balance oldBalance = acc.getBalance();
		Balance newBalance = oldBalance.add(balance);

		acc.setBalance(newBalance);

		accountRepository.save(acc);

		eventDispatcher.dispatch(new AccountBalanceChangedEvent(acc.clone(), oldBalance));

		return ServiceResponseType.OK;
	}

	public ServiceResponseType subtractBalanceFromAccount(String accountId, Balance balance) {
		Account acc = accountRepository.getById(accountId);

		if (acc == null)
			return ServiceResponseType.INVALID_ACCOUNT_ID;

		Balance oldBalance = acc.getBalance();

		/*
		 * If the user paying for the ad doesn't have sufficient money in its account to
		 * pay for the ad, the ad shouldn't be displayed. If the execution got here it's
		 * because there in an error in the AdModelService was the ad got displayed.
		 */
		if (!oldBalance.canSubtract(balance))
			return ServiceResponseType.INSUFFICIENT_ACCOUNT_BALANCE;

		Balance newBalance = oldBalance.subtract(balance);

		acc.setBalance(newBalance);

		accountRepository.save(acc);

		eventDispatcher.dispatch(new AccountBalanceChangedEvent(acc.clone(), oldBalance));

		return ServiceResponseType.OK;
	}

	/**
	 * @param from  {accountId}
	 * @param to    {accountId}
	 * @param value must contain a dot(.) instead of a comma(,). Use
	 *              {@link Contract#convertPaymentValueToDotNotation()} to convert
	 */
	public void transferBalance(String from, String to, String value) {
		Account fromAccount = accountRepository.getById(from);

		if (fromAccount == null) {
			logger.error(
					"Balance transfer failed, payer accountId is not valid // payer: [%s], reciever: [%s], amount: [%s]",
					from, to, value);
			return;
		}

		Account toAccount = accountRepository.getById(to);

		if (toAccount == null) {
			logger.error(
					"Balance transfer failed, reciever accountId is not valid // payer: [%s], reciever: [%s], amount: [%s]",
					from, to, value);
			return;
		}

		Balance balance = null;

		try {
			balance = new Balance(value);
		} catch (BalanceException | NumberFormatException e) {
			logger.error(
					"Balance transfer failed, payment value can't be converted to Balance // payer: [%s], reciever: [%s], amount: [%s]",
					from, to, value);
			return;
		}

		transferBalance(fromAccount, toAccount, balance);
	}

	private void transferBalance(Account from, Account to, Balance balance) {
		ServiceResponseType subtractResponse = subtractBalanceFromAccount(from.getId(), balance); // Remove money from
																									// ad owner

		if (subtractResponse != ServiceResponseType.OK) {
			logger.error(
					"Balance transfer failed, payer account doesn't contain sufficient balance. // payer: [%s], reciever: [%s], amount: [%s]",
					from.getId(), to.getId(), balance.toString());
			return;
		}

		addBalanceToAccount(to.getId(), balance); // Ad money to website owner
	}

	/**
	 * 
	 * @param creatorId
	 * @param value     must contain a dot(.) instead of a comma(,). Use
	 *                  {@link Contract#convertPaymentValueToDotNotation()} to
	 *                  convert
	 * @return <code>true</code> if the account balance is at least the specified
	 *         {value}
	 */
	public boolean hasAccountBalance(String id, String valueAsString) {
		Balance value = null;

		try {
			value = new Balance(valueAsString);
		} catch (BalanceException | NumberFormatException e) {
			return false;
		}

		Account acc = accountRepository.getById(id);

		if (acc == null)
			return false;

		Balance balance = acc.getBalance();

		return balance.canSubtract(value); // If the balance can subtract another value it's because it's greater
	}

}
