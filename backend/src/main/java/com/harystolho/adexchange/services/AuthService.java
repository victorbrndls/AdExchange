package com.harystolho.adexchange.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.auth.TokenService;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.repositories.auth.AuthRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.PasswordSecurity;

@Service
public class AuthService {

	private static final Logger logger = Logger.getLogger(AuthService.class.getName());

	private AuthRepository authRepository;
	private TokenService tokenService;

	@Autowired
	public AuthService(AuthRepository authRepository, TokenService tokenService) {
		this.authRepository = authRepository;
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

		Account response = authRepository.save(account);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<String> login(String email, String password) {
		if (email.length() <= 0 || password.length() <= 0)
			return ServiceResponse.fail("Email or/and password can't be blank");

		Account possibleAccount = authRepository.getByEmail(sanitizeEmail(email));

		if (possibleAccount == null) {
			logger.info(String.format("There is no account with the login email[%s]", email));
			return ServiceResponse.fail("");
		}

		if (!PasswordSecurity.comparePasswords(possibleAccount.getPassword(),
				PasswordSecurity.encryptPassword(password))) {
			logger.info("Passwords are not equal");
			return ServiceResponse.fail("");
		}

		String token = tokenService.generateTokenForAccount(possibleAccount.getId());

		return ServiceResponse.ok(token);
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
		return authRepository.getByEmail(email) != null;
	}

}
