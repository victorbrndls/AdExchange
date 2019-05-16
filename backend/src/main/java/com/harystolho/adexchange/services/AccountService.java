package com.harystolho.adexchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.auth.TokenService;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.account.AccountRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.PasswordSecurity;

@Service
public class AccountService {

	private AccountRepository accountRepository;
	private TokenService tokenService;

	@Autowired
	public AccountService(@Qualifier("cachedAccountRepository") AccountRepository accountRepository,
			TokenService tokenService) {
		this.accountRepository = accountRepository;
		this.tokenService = tokenService;
	}

	public ServiceResponse<Nothing> createAccount(String email, String password) {
		email = sanitizeEmail(email);

		if (!verifyEmail(email))
			return ServiceResponse.error(ServiceResponseType.INVALID_EMAIL);

		if (!verifyPassword(password))
			return ServiceResponse.error(ServiceResponseType.INVALID_PASSWORD);

		if (emailExists(email))
			return ServiceResponse.error(ServiceResponseType.EMAIL_ALREADY_EXISTS);

		Account account = new Account(email, PasswordSecurity.encryptPassword(password));

		accountRepository.save(account);

		return ServiceResponse.ok(null);
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

	/**
	 * Updates the name,
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

	/**
	 * Updates the email and password
	 * 
	 * @param accountId
	 * @return
	 */
	public ServiceResponse<Account> updateAccountAuth(String accountId, String email, String password) {
		email = sanitizeEmail(email);

		Account acc = accountRepository.getById(accountId);

		if (acc == null)
			return ServiceResponse.error(ServiceResponseType.INVALID_ACCOUNT_ID);

		if (!verifyEmail(email))
			return ServiceResponse.error(ServiceResponseType.INVALID_EMAIL);

		if (!verifyPassword(password))
			return ServiceResponse.error(ServiceResponseType.INVALID_PASSWORD);

		if (emailExists(email))
			return ServiceResponse.error(ServiceResponseType.EMAIL_ALREADY_EXISTS);

		acc.setEmail(email);
		acc.setPassword(PasswordSecurity.encryptPassword(password));
		accountRepository.save(acc);

		return ServiceResponse.ok(null);
	}

}
