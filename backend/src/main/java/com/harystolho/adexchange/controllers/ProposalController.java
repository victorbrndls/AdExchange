package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.services.ProposalService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Pair;

@RestController
public class ProposalController {

	private ProposalService proposalService;

	@Autowired
	public ProposalController(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	@GetMapping("/api/v1/proposals/me")
	@CrossOrigin
	/**
	 * @param websiteId
	 * @param adId
	 * @param duration
	 * @param paymentMethod
	 * @param paymentValue
	 * @return the proposals that belong to the account that made the request
	 */
	public ResponseEntity<Object> getAccountProposals() {

		Pair<ServiceResponse, List<Proposal>> response = proposalService.getAccountProposals();

		return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
	}

	@PostMapping("/api/v1/proposals")
	@CrossOrigin
	public ResponseEntity<Object> createProposal(String websiteId, String adId, String duration, String paymentMethod,
			String paymentValue) {

		Pair<ServiceResponse, Proposal> response = proposalService.createProposal(websiteId, adId, duration,
				paymentMethod, paymentValue);

		switch (response.getFist()) {
		case INVALID_WEBSITE_ID:
		case INVALID_AD_ID:
		case INVALID_DURATION:
		case INVALID_PAYMENT_METHOD:
		case INVALID_PAYMENT_VALUE:
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}