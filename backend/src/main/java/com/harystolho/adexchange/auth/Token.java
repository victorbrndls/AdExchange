package com.harystolho.adexchange.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Object used to authenticate users
 * 
 * @author Harystolho
 *
 */
public class Token {

	private String token;
	private Long expiry;

	public Token(String token, Duration duration) {
		this.token = token;
		expiry = Date.from(Instant.now()).getTime() + duration.toMillis();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiry() {
		return expiry;
	}

	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}

	@Override
	public int hashCode() {
		return this.getToken().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Token) {
			Token obj1 = (Token) obj;

			if (this.getToken().equals(obj1.getToken()))
				return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("Token[%s | Expiry: %s]", token, new Date(expiry));
	}

}
