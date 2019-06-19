package com.harystolho.adserver.tracker;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adserver.tracker.repository.UserInteractionRepository;

@Service // TODO move this to Redis in the future
public class UserTrackerService {

	public static final String COOKIE_NAME = "t_ae";

	private UserInteractionRepository userInteractionRepository;

	private UserTrackerService(@Qualifier("userInteractionCache") UserInteractionRepository userTrackerRepository) {
		this.userInteractionRepository = userTrackerRepository;
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
	 * @param interaction
	 * @return <code>true</code> if the tracker has interacted with the interaction.
	 *         The interaction is an identifier for some other object, for example
	 *         if the user identified by this tracker has clicked some ad, you can
	 *         map this tracker to the ad id.
	 */
	public boolean hasTrackerInteractedWith(Tracker tracker, String interaction) {
		Set<String> listByCookie = getInteractions(tracker.getCookie().getValue());
		if (listByCookie != null && listByCookie.contains(interaction))
			return true;

		Set<String> listByClientAddr = getInteractions(tracker.getClientAddr());
		if (listByClientAddr != null && listByClientAddr.contains(interaction))
			return true;

		return false;
	}

	/**
	 * Some prefix should be used before the interactor id to make sure they don't
	 * collide
	 * 
	 * @param tracker
	 * @param interactionId
	 */
	public void interactTrackerWith(Tracker tracker, String interactionId) {
		String cookie = tracker.getCookie().getValue();
		String addr = tracker.getClientAddr();

		interactTrackerIdentifierWith(cookie, interactionId);
		interactTrackerIdentifierWith(addr, interactionId);
	}

	private void interactTrackerIdentifierWith(String interactorId, String interactionId) {
		UserInteraction ui = Optional.ofNullable(userInteractionRepository.getByInteractorId(interactorId))
				.orElse(createUserInteractor(interactorId));

		ui.addInteraction(interactionId);
		userInteractionRepository.save(ui);
	}

	private UserInteraction createUserInteractor(String interactorId) {
		UserInteraction ui = new UserInteraction();
		ui.setInteractorId(interactorId);

		return ui;
	}

	private Set<String> getInteractions(String interactorId) {
		UserInteraction ui = userInteractionRepository.getByInteractorId(interactorId);

		return ui != null ? ui.getInteractions() : new HashSet<>();
	}

}
