package com.harystolho.adServer.tracker;

import javax.servlet.http.Cookie;

import org.springframework.lang.Nullable;

public class Tracker {

	@Nullable
	private final Cookie cookie;

	@Nullable
	private final String clientAddr;

	public Tracker(Cookie cookie, String clientAddr) {
		this.cookie = cookie;
		this.clientAddr = clientAddr;
	}

	public Cookie getCookie() {
		return cookie;
	}

	public String getClientAddr() {
		return clientAddr;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (obj instanceof Tracker) {
			Tracker other = (Tracker) obj;

			if (!this.clientAddr.equals(other.clientAddr))
				return false;

			if (!this.cookie.getValue().equals(other.cookie.getValue()))
				return false;

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return cookie.getValue().hashCode() * clientAddr.hashCode();
	}

}
