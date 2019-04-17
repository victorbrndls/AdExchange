package com.harystolho.adexchange.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AuthService {

	public Pair<ServiceResponse, Nothing> createAccount(String email, String password) {
		if (!verifyEmail(email))
			return Pair.of(ServiceResponse.INVALID_EMAIL, null);

		if (!verifyPassword(email))
			return Pair.of(ServiceResponse.INVALID_PASSWORD, null);

		return Pair.of(ServiceResponse.OK, null);
	}

	/**
	 * @param email
	 * @return true if the email is valid
	 */
	private boolean verifyEmail(String email) {
		return false;
	}

	/**
	 * @param email
	 * @return true if the password is valid
	 */
	private boolean verifyPassword(String email) {
		// TODO Auto-generated method stub
		return false;
	}

}
