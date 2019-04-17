package com.harystolho.adexchange.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AuthService {

	
	
	public Pair<ServiceResponse, Nothing> createAccount(String email, String password) {
		email = sanitizeEmail(email);

		if (!verifyEmail(email))
			return Pair.of(ServiceResponse.INVALID_EMAIL, null);

		if (!verifyPassword(password))
			return Pair.of(ServiceResponse.INVALID_PASSWORD, null);

		return Pair.of(ServiceResponse.OK, null);
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

}
