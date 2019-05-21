package com.harystolho.adServer.tracker;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.AEUtils;

@Service
public class UserTrackerService {

	public static final String COOKIE_NAME = "t_ae";

	// when user goes to /redict add a cookie
	// req.getRemoteAddr()
	// req.getRemoteHost()

	public boolean isTrackerValid(Tracker tracker) {
		Cookie cookie = tracker.getCookie();
		String clientAddr = tracker.getClientAddr();

		if (cookie == null || clientAddr == null)
			return false;// TODO check that the address is not local, > 127.0... > 10.0...

		if (cookie != null)
			if (cookie.getValue() == null)
				return false;

		return true;
	}

	public Tracker createTracker(String clientAddr) {
		return new Tracker(createCookieTracker(), clientAddr);
	}

	public Cookie createCookieTracker() {
		Cookie cookie = new Cookie(COOKIE_NAME, AEUtils.generateUUIDString(4));
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);

		return cookie;
	}

}
