package com.harystolho.adServer.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adServer.events.EventDispatcher;
import com.harystolho.adServer.events.spot.SpotClickedEvent;
import com.harystolho.adServer.services.UrlRedirecterService;
import com.harystolho.adServer.tracker.Tracker;
import com.harystolho.adServer.tracker.UserTrackerService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;

@RestController
@CrossOrigin
public class UrlRedirectorController {

	public static final String REDIRECT_ENDPOINT = "/redirect";

	private UrlRedirecterService urlRedirectirService;
	private EventDispatcher eventDispatcher;
	private UserTrackerService userTrackerService;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirectorService, EventDispatcher eventDispatcher,
			UserTrackerService userTrackerService) {
		this.urlRedirectirService = urlRedirectorService;
		this.eventDispatcher = eventDispatcher;
		this.userTrackerService = userTrackerService;
	}

	@GetMapping(path = REDIRECT_ENDPOINT + "/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res, @PathVariable String id) {
		ServiceResponse<String> response = urlRedirectirService.getUrlUsingRequestPath(req.getRequestURI());

		Tracker tracker = new Tracker(AEUtils.getCookieByName(req.getCookies(), UserTrackerService.COOKIE_NAME),
				req.getRemoteAddr());

		if (!userTrackerService.isTrackerValid(tracker)) {
			tracker = userTrackerService.createTracker(req.getRemoteAddr());
			res.addCookie(tracker.getCookie());
		}

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

}
