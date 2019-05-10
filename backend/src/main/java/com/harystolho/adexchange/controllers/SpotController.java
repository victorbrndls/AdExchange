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

import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class SpotController {

	private SpotService spotService;

	@Autowired
	private SpotController(SpotService spotService) {
		this.spotService = spotService;
	}

	@PostMapping("/api/v1/spots")
	public ResponseEntity<Object> createOrUpdateSpot(@RequestAttribute("ae.accountId") String accountId, String id,
			String name, String contractId, String fallbackAdId) {

		ServiceResponse<Spot> response = spotService.createOrUpdateSpot(accountId, id, name, contractId, fallbackAdId);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		}
	}

	@GetMapping("/api/v1/spots/{id}")
	public ResponseEntity<Object> getSpot(@RequestAttribute("ae.accountId") String accountId, @PathVariable String id,
			@RequestParam(defaultValue = "") String embed) {

		ServiceResponse<Spot> response = spotService.getSpot(accountId, id, embed);

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
	public ResponseEntity<Object> getAccountSpots(@RequestAttribute("ae.accountId") String accountId,
			@RequestParam(defaultValue = "") String embed) {

		ServiceResponse<List<Spot>> response = spotService.getSpotsByAccountId(accountId, embed);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}
	}

	@DeleteMapping("/api/v1/spots/{id}")
	public ResponseEntity<Object> deleteSpot(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Spot> response = spotService.deleteSpot(accountId, id);

		switch (response.getErrorType()) {
		case UNAUTHORIZED:
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getErrorType());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response.getReponse());
		}
	}

}
