package com.harystolho.adexchange.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.auth.TokenService;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.account.AccountRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.PasswordSecurity;

@Service
public class AccountService {

	private static final Logger logger = LogManager.getLogger();

	private AccountRepository accountRepository;
	private TokenService tokenService;

	@Autowired
	public AccountService(@Qualifier("cachedAccountRepository") AccountRepository accountRepository,
			TokenService tokenService) {
		this.accountRepository = accountRepository;
		this.tokenService = tokenService;
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

		if (verifyAccountBalance(acc)) {
			return ServiceResponse.ok(acc.getBalance());
		} else {
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_BALANCE);
		}
	}

	/**
	 * This method verifies that the user balance is valid, if it is not valid then
	 * it prints an error for devs to notice and fix it
	 * 
	 * @param account
	 * @return
	 */
	private boolean verifyAccountBalance(Account account) {
		if (!StringUtils.hasText(account.getBalance())) {
			logger.error("Invalid balace on account - AccountId: [{}], Balance: [{}]", account.getId(),
					account.getBalance());
			return false;
		}

		if (!AEUtils.isValueValidForAccountBalance(account.getBalance())) {
			logger.error("Invalid balace on account - AccountId: [{}], Balance: [{}]", account.getId(),
					account.getBalance());
			return false;
		}

		return true;
	}

}
