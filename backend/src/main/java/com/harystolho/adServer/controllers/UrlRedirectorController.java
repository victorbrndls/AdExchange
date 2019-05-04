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

	UrlRedirecterService urlRedirecterService;

	@Autowired
	private UrlRedirectorController(UrlRedirecterService urlRedirecterService) {
		this.urlRedirecterService = urlRedirecterService;
	}

	@GetMapping("/serve/v1/redirect/{id}")
	public void redirect(HttpServletRequest req, HttpServletResponse res) {
		ServiceResponse<String> response = urlRedirecterService.getUrlUsingRequestPath(req.getRequestURI());

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
