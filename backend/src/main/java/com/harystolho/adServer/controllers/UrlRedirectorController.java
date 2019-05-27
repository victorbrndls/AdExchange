package com.harystolho.adserver.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.spot.SpotClickedEvent;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;

@RestController
@CrossOrigin
public class UrlRedirectorController {

	public static final String REDIRECT_ENDPOINT = "/redirect";

	private UrlRedirecterService urlRedirectorService;
	private EventDispatcher eventDispatcher;
	private UserTrackerService userTrackerService;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirectorService, EventDispatcher eventDispatcher,
			UserTrackerService userTrackerService) {
		this.urlRedirectorService = urlRedirectorService;
		this.eventDispatcher = eventDispatcher;
		this.userTrackerService = userTrackerService;
	}

	@GetMapping(path = REDIRECT_ENDPOINT + "/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res, @PathVariable String id) {
		ServiceResponse<String> response = urlRedirectorService.getRefUrlUsingRequestPath(req.getRequestURI());

		Tracker tracker = addTrackerToRequest(req, res);

		try {
			switch (response.getErrorType()) {
			case OK:
				res.sendRedirect(response.getReponse());

				eventDispatcher.dispatch(new SpotClickedEvent(id, tracker));
				break;
			default:
				res.getWriter().write("No mapping for url id"); // TODO redirect back to origin page
				break;
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

	/**
	 * Adds a tracker the the request if it doesn't contain one
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	private Tracker addTrackerToRequest(HttpServletRequest req, HttpServletResponse res) {
		Tracker tracker = new Tracker(AEUtils.getCookieByName(req.getCookies(), UserTrackerService.COOKIE_NAME),
				req.getRemoteAddr());

		if (!userTrackerService.isTrackerValid(tracker)) {
			tracker = userTrackerService.createTracker(req.getRemoteAddr());
			res.addCookie(tracker.getCookie());
		}

		return tracker;

	}

}
