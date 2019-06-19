package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.controllers.models.WebsiteBuilderModel;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.WebsiteService;
import com.harystolho.adexchange.utils.AEUtils;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class WebsiteController {

	private WebsiteService websiteService;

	@Autowired
	public WebsiteController(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}

	@GetMapping("/api/v1/websites")
	public ResponseEntity<Object> getWebsites(@RequestParam(defaultValue = "") String categories) {
		ServiceResponse<List<Website>> response = websiteService.getWebsites(categories);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		}
	}

	@GetMapping("/api/v1/websites/{id}")
	public ResponseEntity<Object> getWebsiteById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {
		ServiceResponse<Website> response = websiteService.getWebsiteById(id);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			ObjectNode node = (ObjectNode) new ObjectMapper().valueToTree(response.getReponse());
			node.put("owner", response.getReponse().getAccountId().equals(accountId));
			node.remove("accountId");

			return ResponseEntity.status(HttpStatus.CREATED).body(node);
		}
	}

	@PostMapping("/api/v1/websites")
	public ResponseEntity<Object> createorUpdateWebsite(@RequestAttribute("ae.accountId") String accountId, String id,
			@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String url,
			@RequestParam(defaultValue = "") String monthlyImpressions, String logoURL,
			@RequestParam(defaultValue = "") String description,
			@RequestParam(defaultValue = "null") String categories) {

		ServiceResponse<Website> response = websiteService.createWebsite(new WebsiteBuilderModel()
				.setAccountId(accountId).setId(id).setName(name).setUrl(url).setLogoUrl(logoURL)
				.setDescription(description).setCategories(categories).setMonthlyImpressions(monthlyImpressions));

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		}
	}

	@DeleteMapping("/api/v1/websites/{id}")
	public ResponseEntity<Object> deleteWebsite(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Website> response = websiteService.deleteWebsite(accountId, id);

		switch (response.getErrorType()) {
		case UNAUTHORIZED:
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}

}
