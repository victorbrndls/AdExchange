package com.harystolho.adServer.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adServer.events.EventDispatcher;
import com.harystolho.adServer.events.SpotClickedEvent;
import com.harystolho.adServer.events.SpotEventHandler;
import com.harystolho.adServer.services.UrlRedirecterService;
import com.harystolho.adexchange.services.ServiceResponse;

@RestController
@CrossOrigin
public class UrlRedirectorController {

	public static final String REDIRECT_ENDPOINT = "/redirect";

	private UrlRedirecterService urlRedirectirService;
	private EventDispatcher eventDispatcher;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirectorService, EventDispatcher eventDispatcher) {
		this.urlRedirectirService = urlRedirectorService;
		this.eventDispatcher = eventDispatcher;
	}

	@GetMapping(path = REDIRECT_ENDPOINT + "/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res) {
		ServiceResponse<String> response = urlRedirectirService.getUrlUsingRequestPath(req.getRequestURI());

		try {
			switch (response.getErrorType()) {
			case OK:
				res.sendRedirect(response.getReponse());
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
