package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

		Pair<ServiceResponse, List<Website>> response = websiteService.getWebsites();

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

	@PostMapping("/api/v1/websites")
	@CrossOrigin // TODO only allow crossorigin from 8081
	public ResponseEntity<Object> createWebsite(@RequestParam("url") String url,
			@RequestParam("logoURL") String logoURL, @RequestParam("description") String description) {

		Pair<ServiceResponse, Website> response = websiteService.createWebsite(url, logoURL, description);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}
