package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.WebsiteService;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Pair;

@RestController
public class WebsiteController {

	private WebsiteService websiteService;

	@Autowired
	public WebsiteController(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}

	@GetMapping("/api/v1/websites")
	@CrossOrigin
	public ResponseEntity<Object> getWebsites() {
		ServiceResponse<List<Website>> response = websiteService.getWebsites();

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		}

	}

	@GetMapping("/api/v1/websites/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getWebsiteById(@PathVariable String id) {
		ServiceResponse<Website> response = websiteService.getWebsiteById(id);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		}

	}

	@PostMapping("/api/v1/websites")
	@CrossOrigin // TODO only allow crossorigin from 8081
	public ResponseEntity<Object> createWebsite(@RequestAttribute("ae.accountId") String accountId, String name,
			String url, String logoURL, String description, String categories) {

		ServiceResponse<Website> response = websiteService.createWebsite(accountId, name, url, logoURL, description,
				categories);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		}

	}

}
