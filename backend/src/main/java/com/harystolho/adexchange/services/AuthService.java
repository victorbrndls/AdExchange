package com.harystolho.adexchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.AuthRepository;
import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adexchange.utils.PasswordSecurity;

@Service
public class AuthService {

	private AuthRepository authRepository;

	@Autowired
	public AuthService(AuthRepository authRepository) {
		this.authRepository = authRepository;
	}

	public Pair<ServiceResponse, Nothing> createAccount(String email, String password) {
		email = sanitizeEmail(email);

		if (!verifyEmail(email))
			return Pair.of(ServiceResponse.INVALID_EMAIL, null);

		if (!verifyPassword(password))
			return Pair.of(ServiceResponse.INVALID_PASSWORD, null);

		if (emailExists(email))
			return Pair.of(ServiceResponse.EMAIL_ALREADY_EXISTS, null);

		Account account = new Account(email, password);

		Pair<RepositoryResponse, Account> response = authRepository.saveAccount(account);

		if (response.getFist() == RepositoryResponse.CREATED)
			return Pair.of(ServiceResponse.OK, null);

		return Pair.of(ServiceResponse.FAIL, null);
	}

	public Pair<ServiceResponse, String> login(String email, String password) {
		if (email.length() <= 0 || password.length() <= 0)
			return Pair.of(ServiceResponse.FAIL, "");

		Account possibleAccount = authRepository.getAccountByEmail(sanitizeEmail(email));

		if (possibleAccount == null)
			return Pair.of(ServiceResponse.FAIL, "");

		if (!PasswordSecurity.comparePasswords(possibleAccount.getPassword(),
				PasswordSecurity.encryptPassword(password)))
			return Pair.of(ServiceResponse.FAIL, "");

		return Pair.of(ServiceResponse.OK, "123456789abc");
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
		return email.contains("@"); // TODO fix email verification
	}

	/**
	 * @param email
	 * @return true if the password is valid
	 */
	private boolean verifyPassword(String password) {
		return password.length() >= 5;
	}

	private boolean emailExists(String email) {
		return authRepository.getAccountByEmail(email) != null;
	}

}
