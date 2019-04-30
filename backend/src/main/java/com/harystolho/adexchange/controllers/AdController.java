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

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.Nothing;

@RestController
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}

	@GetMapping("/api/v1/ads/me")
	@CrossOrigin
	public ResponseEntity<Object> getAccountAds(@RequestAttribute("ae.accountId") String accountId) {
		ServiceResponse<List<Ad>> response = adService.getAdsByAccountId(accountId);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	@GetMapping("/api/v1/ads/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getAdById(@PathVariable String id) {
		ServiceResponse<Ad> response = adService.getAdById(id);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	@PostMapping("/api/v1/ads")
	@CrossOrigin
	public ResponseEntity<Object> createAd(@RequestAttribute("ae.accountId") String accountId, String name, String type,
			String refUrl, //
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "bgColor", required = false) String bgColor,
			@RequestParam(value = "textColor", required = false) String textColor,
			@RequestParam(value = "imageUrl", required = false) String imageUrl) {

		ServiceResponse<Ad> response = adService.createAd(accountId, name, type, refUrl, text, bgColor, textColor,
				imageUrl);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	@DeleteMapping("/api/v1/ads/{id}")
	@CrossOrigin
	public ResponseEntity<Object> deleteAd(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {
		ServiceResponse<Nothing> response = adService.deleteAdById(accountId, id);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response.getReponse());
		}

	}

}
