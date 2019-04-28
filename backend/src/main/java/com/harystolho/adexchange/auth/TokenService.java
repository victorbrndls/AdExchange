package com.harystolho.adexchange.auth;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

	private final Duration tokenDuration = Duration.ofDays(7);

	private Map<Token, String> tokenToAccountId;

	public TokenService() {
		tokenToAccountId = new ConcurrentHashMap<>();

		tokenToAccountId.put(new Token(
				"eca70362be2f4b3eb7013210fd71e940cdc3a0a86bf743de88a344a70848a48bc5a58e9d65f443a7b1e9ccd62ef33b204aca4756fdf84e8cba3629a02f49d90f",
				tokenDuration), "5cc47954f669ff0e3cacbdd0");

		tokenToAccountId.put(new Token(
				"8e9d65f443a7b1e9cceca70362be2f4b3eb7013210fd71e86bf743de88a344a70848a48bc5a5df84e8cba36d62ef33b204aca4756fc3a0a29a02f49d90f940cd",
				tokenDuration), "5cc48320f669ff0e3cacbddf");
	}

	public String generateTokenForAccount(String accountId) {
		Token token = generateToken();

		tokenToAccountId.put(token, accountId);

		return token.getToken();
	}

	/**
	 * Generates a unique token
	 * 
	 * @return
	 */
	private Token generateToken() {
		String tokenValue = generateUUID(4);

		Token token = new Token(tokenValue, tokenDuration);

		if (tokenExists(token))
			return generateToken();

		return token;
	}

	private boolean tokenExists(Token token) {
		return tokenToAccountId.containsKey(token);
	}

	public Optional<String> getAccountIdByTokenValue(String tokenValue) {
		Token token = new Token(tokenValue, Duration.ZERO);

		String accountId = tokenToAccountId.get(token);

		if (accountId != null)
			return Optional.of(accountId);

		return Optional.empty();
	}

	/**
	 * Concatenates UUIDs together to make a big token
	 * 
	 * @param size
	 * @return
	 */
	private String generateUUID(int size) {
		String uuid = "";

		for (int i = 0; i < size; i++) {
			uuid += UUID.randomUUID().toString().replaceAll("-", "");
		}

		return uuid;
	}

}
