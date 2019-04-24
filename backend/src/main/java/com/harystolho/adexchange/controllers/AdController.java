package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.Ad;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.WebsiteService;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Pair;

@RestController
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}

	/*
	 * @GetMapping("/api/v1/ads")
	 * 
	 * @CrossOrigin public ResponseEntity<Object> getAdsByAccount() {
	 * 
	 * Pair<ServiceResponse, List<Website>> response = websiteService.getWebsites();
	 * 
	 * switch (response.getFist()) { case FAIL: return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); default: return
	 * ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond()); }
	 * 
	 * }
	 */

	@PostMapping("/api/v1/ads")
	@CrossOrigin
	public ResponseEntity<Object> createAd(@RequestParam("token") String token, @RequestParam("name") String name) {
		Pair<ServiceResponse, Ad> response = adService.createAd(name);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}
