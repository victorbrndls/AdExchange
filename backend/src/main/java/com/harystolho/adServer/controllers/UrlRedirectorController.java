package com.harystolho.adserver.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.spots.events.SpotClickedEvent;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adserver.services.TrackableRequestService;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.tracker.Tracker;

@RestController
@CrossOrigin
public class UrlRedirectorController {

	public static final String REDIRECT_ENDPOINT = "/redirect";

	private UrlRedirecterService urlRedirectorService;
	private EventDispatcher eventDispatcher;
	private TrackableRequestService trackableRequestService;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirectorService, EventDispatcher eventDispatcher,
			TrackableRequestService trackableRequestService) {
		this.urlRedirectorService = urlRedirectorService;
		this.eventDispatcher = eventDispatcher;
		this.trackableRequestService = trackableRequestService;
	}

	@GetMapping(path = REDIRECT_ENDPOINT + "/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res, @PathVariable String id) {
		Tracker tracker = trackableRequestService.addTrackerToRequest(req, res);
		
		ServiceResponse<String> response = urlRedirectorService.getRefUrlUsingRequestPath(req.getRequestURI());

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
