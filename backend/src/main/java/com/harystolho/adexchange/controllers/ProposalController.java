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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.services.ProposalService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Nothing;
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
	 * @return the proposals that belong to the account that made the request
	 */
	public ResponseEntity<Object> getProposals(@RequestAttribute("ae.accountId") String accountId) {

		Pair<ServiceResponse, ProposalsHolder> response = proposalService.getProposalsByAccountId(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
	}

	@GetMapping("/api/v1/proposals/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getProposalById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		Pair<ServiceResponse, Proposal> response = proposalService.getProposalById(accountId, id);

		ObjectNode node = (ObjectNode) new ObjectMapper().valueToTree(response.getSecond());
		node.put("owner", response.getSecond().getCreatorAccountId().equals(accountId));

		return ResponseEntity.status(HttpStatus.OK).body(node);
	}

	@GetMapping("/api/v1/proposals/batch")
	@CrossOrigin
	/**
	 * @param ids list of proposals ids separated by comma
	 * @return
	 */
	public ResponseEntity<Object> getProposalsById(String ids) {
		Pair<ServiceResponse, List<Proposal>> response = proposalService.getProposalsById(ids);

		return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
	}

	@PostMapping("/api/v1/proposals")
	@CrossOrigin
	public ResponseEntity<Object> createProposal(@RequestAttribute("ae.accountId") String accountId, String websiteId,
			String adId, String duration, String paymentMethod, String paymentValue) {

		Pair<ServiceResponse, Proposal> response = proposalService.createProposal(accountId, websiteId, adId, duration,
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

	@DeleteMapping("/api/v1/proposals/{id}")
	@CrossOrigin
	public ResponseEntity<Object> deleteProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		Pair<ServiceResponse, Nothing> response = proposalService.deleteProposalById(accountId, id);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

	}

	@PostMapping("/api/v1/proposals/reject/{id}")
	@CrossOrigin
	public ResponseEntity<Object> rejectProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		Pair<ServiceResponse, Nothing> response = proposalService.rejectProposalById(accountId, id);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PostMapping("/api/v1/proposals/revision/{id}")
	@CrossOrigin
	public ResponseEntity<Object> reviewProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id, String duration, String paymentMethod, String paymentValue) {

		Pair<ServiceResponse, Nothing> response = proposalService.reviewProposal(accountId, id, duration, paymentMethod,
				paymentValue);

		switch (response.getFist()) {
		case INVALID_DURATION:
		case INVALID_PAYMENT_METHOD:
		case INVALID_PAYMENT_VALUE:
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PostMapping("/api/v1/proposals/accept/{id}")
	@CrossOrigin
	public ResponseEntity<Object> acceptProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		Pair<ServiceResponse, Nothing> response = proposalService.acceptProposal(accountId, id);

		switch (response.getFist()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
}
