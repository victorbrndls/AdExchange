package com.harystolho.adServer.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adServer.services.UrlRedirecterService;
import com.harystolho.adexchange.services.ServiceResponse;

@RestController
@CrossOrigin
public class UrlRedirectorController {

	public static final String REDIRECT_ENDPOINT = "/redirect";

	private UrlRedirecterService urlRedirectirService;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirectorService) {
		this.urlRedirectirService = urlRedirectorService;
	}

	@GetMapping(path = REDIRECT_ENDPOINT + "/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res) {
		ServiceResponse<String> response = urlRedirectirService.getUrlUsingRequestPath(req.getRequestURI());

		try {
			switch (response.getErrorType()) {
			case FAIL:
				res.getWriter().write("No mapping for this id");
				break;
			default:
				res.sendRedirect(response.getReponse());
				break;
			}
		} catch (Exception e) {
			// Do nothing
		}
	}

}
