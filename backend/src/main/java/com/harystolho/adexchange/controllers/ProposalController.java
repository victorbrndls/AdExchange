package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

	@PostMapping("/api/v1/proposals")
	@CrossOrigin
	public ResponseEntity<Object> createProposal() {

		Pair<ServiceResponse, Proposal> response = proposalService.createProposal();

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getSecond());
		}

	}

}
