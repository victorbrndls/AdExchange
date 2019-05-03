package com.harystolho.adexchange.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.ads.Ad;
import com.harystolho.adexchange.services.AdService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.Nothing;

@RestController
@CrossOrigin
public class AdController {

	private AdService adService;

	@Autowired
	public AdController(AdService adService) {
		this.adService = adService;
	}

	@GetMapping("/api/v1/ads/me")
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
	public ResponseEntity<Object> getAdById(@PathVariable String id) {
		ServiceResponse<Ad> response = adService.getAdById(id);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	@GetMapping("/api/v1/ads/batch")
	public ResponseEntity<Object> getAdsById(String ids) {
		ServiceResponse<List<Ad>> response = adService.getAdsById(ids);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	/**
	 * POST and PUT are in the same method because they share most fields
	 * 
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT }, path = { "/api/v1/ads", "/api/v1/ads/{id}" })
	public ResponseEntity<Object> createOrUpdateAd(HttpServletRequest req,
			@RequestAttribute("ae.accountId") String accountId, String name, String type, String refUrl, //
			@RequestParam(required = false) String text, @RequestParam(required = false) String bgColor,
			@RequestParam(required = false) String textColor, @RequestParam(required = false) String imageUrl,
			@PathVariable(required = false) String id) {

		ServiceResponse<Ad> response = ServiceResponse.fail("Http method must either be PUT or POST");

		if (req.getMethod().equals("POST")) {
			response = adService.createAd(accountId, name, type, refUrl, text, bgColor, textColor, imageUrl);
		} else if (req.getMethod().equals("PUT")) {
			response = adService.updateAd(accountId, id, name, type, refUrl, text, bgColor, textColor, imageUrl);
		}

		switch (response.getErrorType()) {
		case UNAUTHORIZED:
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getErrorType());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

	@DeleteMapping("/api/v1/ads/{id}")
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
