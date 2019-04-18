package com.harystolho.adexchange.auth;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

	private Map<Token, String> tokenToAccountId;

	public TokenService() {
		tokenToAccountId = new ConcurrentHashMap<>();
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
		String tokenValue = UUID.randomUUID().toString().replaceAll("-", "")
				+ UUID.randomUUID().toString().replaceAll("-", "");

		Token token = new Token(tokenValue, Duration.ofDays(7));

		if (tokenExists(token))
			return generateToken();

		return token;
	}

	private boolean tokenExists(Token token) {
		return tokenToAccountId.containsKey(token);
	}

}
