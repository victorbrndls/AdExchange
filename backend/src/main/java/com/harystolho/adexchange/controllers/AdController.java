package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping("/api/v1/ads")
	@CrossOrigin
	public ResponseEntity<Object> createAd(@RequestParam("name") String name, @RequestParam("type") String type,
			@RequestParam("refUrl") String refUrl, //
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "bgColor", required = false) String bgColor,
			@RequestParam(value = "textColor", required = false) String textColor,
			@RequestParam(value = "imageUrl", required = false) String imageUrl) {

		Pair<ServiceResponse, Ad> response = adService.createAd(name, type, refUrl, text, bgColor, textColor, imageUrl);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}
