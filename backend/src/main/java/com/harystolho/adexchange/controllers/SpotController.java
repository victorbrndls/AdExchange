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

import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;

@RestController
public class SpotController {

	private SpotService spotService;

	@Autowired
	private SpotController(SpotService spotService) {
		this.spotService = spotService;
	}

	@PostMapping("/api/v1/spots")
	@CrossOrigin
	public ResponseEntity<Object> createOrUpdateSpot(@RequestAttribute("ae.accountId") String accountId, String id,
			String name, String contractId, String adId) {

		ServiceResponse<Spot> response = spotService.createSpot(accountId, id, name, contractId, adId);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}
	}

	@GetMapping("/api/v1/spots/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getSpot(@RequestAttribute("ae.accountId") String accountId, @PathVariable String id) {

		ServiceResponse<Spot> response = spotService.getSpot(accountId, id);

		switch (response.getErrorType()) {
		case UNAUTHORIZED:
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getErrorType());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}
	}

	@GetMapping("/api/v1/spots/me")
	@CrossOrigin
	public ResponseEntity<Object> getAccountSpot(@RequestAttribute("ae.accountId") String accountId) {

		ServiceResponse<List<Spot>> response = spotService.getSpotsByAccountId(accountId);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}
	}

}
