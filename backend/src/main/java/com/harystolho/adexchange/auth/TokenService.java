package com.harystolho.adexchange.auth;

import java.time.Duration;
import java.util.Map;
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
				"4ea6334f600f4dd3bef1c39e036b931b85dfbcea57fa4b47b9561f53c2eac979ec044707015a4143be0b3f793558c99462a8b162e06f4e8586be2b203de5643e",
				tokenDuration), "123456789");
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
