package com.harystolho.adexchange.auth;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Manages the tokens used to authenticate users
 * 
 * @author Harystolho
 *
 */
@Service
public class AuthService {

	private Map<String, String> tokenToAccountId;

	public AuthService() {
		tokenToAccountId = new ConcurrentHashMap<>();

		tokenToAccountId.put(
				"eca70362be2f4b3eb7013210fd71e940cdc3a0a86bf743de88a344a70848a48bc5a58e9d65f443a7b1e9ccd62ef33b204aca4756fdf84e8cba3629a02f49d90f",
				"5cc47954f669ff0e3cacbdd0");
	}

	public Optional<String> getAccountIdByToken(String token) {
		String accountId = tokenToAccountId.get(token);

		if (accountId != null)
			return Optional.of(accountId);

		return Optional.empty();
	}

	public String generateTokenForAccount(String accountId) {
		String token = generateToken();

		tokenToAccountId.put(token, accountId);

		return token;
	}

	/**
	 * Generates a unique token
	 * 
	 * @return
	 */
	private String generateToken() {
		String token = generateTokenValue(4);

		if (tokenToAccountId.containsKey(token))
			return generateToken();

		return token;
	}

	/**
	 * @param size a greater number means the value has more chars
	 * @return
	 */
	private String generateTokenValue(int size) {
		StringBuilder value = new StringBuilder();

		for (int i = 0; i < size; i++) {
			value.append(UUID.randomUUID().toString().replaceAll("-", ""));
		}

		return value.toString();
	}

}
