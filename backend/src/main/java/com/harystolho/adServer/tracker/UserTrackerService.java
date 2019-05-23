package com.harystolho.adServer.tracker;

import java.util.List;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.harystolho.adexchange.utils.AEUtils;

// when user goes to /redict add a cookie
// req.getRemoteAddr()
// req.getRemoteHost()

@Service
public class UserTrackerService {

	public static final String COOKIE_NAME = "t_ae";

	// Maps a tracker id(cookie value, remote addr) to some id
	private final MultiValueMap<String, String> trackerIdToInteraction;

	private UserTrackerService() {
		this.trackerIdToInteraction = new LinkedMultiValueMap<>();
	}

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

	/**
	 * @param tracker
	 * @param interactor
	 * @return <code>true</code> if the tracker has interacted with the interactor.
	 *         The interactor is an identifier for some other object, for example if
	 *         the user identified by this tracker has clicked some ad, you can map
	 *         this tracker to the ad id.
	 */
	public boolean hasTrackerInteractedWith(Tracker tracker, String interactor) {
		List<String> listByCookie = trackerIdToInteraction.get(tracker.getCookie().getValue());
		if (listByCookie != null && listByCookie.contains(interactor))
			return true;

		List<String> listByClientAddr = trackerIdToInteraction.get(tracker.getClientAddr());
		if (listByClientAddr != null && listByClientAddr.contains(interactor))
			return true;

		return false;
	}

	/**
	 * Some prefix should be used before the interactor id to make sure they don't
	 * collide
	 * 
	 * @param tracker
	 * @param interactorId
	 */
	public void interactTrackerWith(Tracker tracker, String interactorId) {
		trackerIdToInteraction.add(tracker.getCookie().getValue(), interactorId);
		trackerIdToInteraction.add(tracker.getClientAddr(), interactorId);
	}

}
