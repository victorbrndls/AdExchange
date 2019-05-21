package com.harystolho.adServer.services;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

@Service
public class UserTrackerService {

	public static final String COOKIE_NAME = "t_ae";

	// when user goes to /redict add a cookie
	// req.getRemoteAddr()
	// req.getRemoteHost()

	public Cookie createCookieTracker() {
		Cookie cookie = new Cookie(COOKIE_NAME, "123");
		cookie.setSecure(true);
		cookie.setMaxAge(Integer.MAX_VALUE);

		return cookie;
	}

	public boolean isTrackerValid(Cookie cookie) {
		return false;
	}

}
