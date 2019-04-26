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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Pair;

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
		Pair<ServiceResponse, List<Ad>> response = adService.getAdsByAccountId(accountId);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
		}

	}

	@GetMapping("/api/v1/ads/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getAdById(@PathVariable String id) {
		Pair<ServiceResponse, Ad> response = adService.getAdById(id);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
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

		Pair<ServiceResponse, Ad> response = adService.createAd(accountId, name, type, refUrl, text, bgColor, textColor, imageUrl);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}
