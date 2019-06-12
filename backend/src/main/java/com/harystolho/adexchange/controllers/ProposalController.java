package com.harystolho.adexchange.controllers;

import java.util.ArrayList;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.services.ProposalService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.JsonResponse;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class ProposalController {

	private ProposalService proposalService;

	@Autowired
	public ProposalController(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	@GetMapping("/api/v1/proposals/me")
	/**
	 * @return the proposals that belong to the account that made the request
	 */
	public ResponseEntity<Object> getProposals(@RequestAttribute("ae.accountId") String accountId,
			@RequestParam(defaultValue = "") String embed) {

		ServiceResponse<List<Proposal>> response = proposalService.getProposalsByAccountId(accountId, embed);

		List<Proposal> inSent = new ArrayList<>();
		List<Proposal> inNew = new ArrayList<>();

		for (Proposal p : response.getReponse()) {
			if (p.getProposerId().equals(accountId)) { // User created the proposal
				if (p.isInProposerSent()) {
					inSent.add(p);
				} else {
					inNew.add(p);
				}
			} else { // User created the website that received the proposal
				if (p.isInProposerSent()) {
					inNew.add(p);
				} else {
					inSent.add(p);
				}
			}
		}

		return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.of("sent", inSent).pair("new", inNew).build());
	}

	@GetMapping("/api/v1/proposals/{id}")
	public ResponseEntity<Object> getProposalById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Proposal> response = proposalService.getProposalById(accountId, id);

		ObjectNode node = (ObjectNode) new ObjectMapper().valueToTree(response.getReponse());
		node.put("owner", response.getReponse().getProposerId().equals(accountId));

		return ResponseEntity.status(HttpStatus.OK).body(node);
	}

	@PostMapping("/api/v1/proposals")
	public ResponseEntity<Object> createProposal(@RequestAttribute("ae.accountId") String accountId, String websiteId,
			String adId, String duration, String paymentMethod, String paymentValue) {

		ServiceResponse<Proposal> response = proposalService.createProposal(accountId, websiteId, adId, duration,
				paymentMethod, paymentValue);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		}
	}

	@DeleteMapping("/api/v1/proposals/{id}")
	public ResponseEntity<Object> deleteProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponseType response = proposalService.deleteProposalById(accountId, id);

		switch (response) {
		case PROPOSAL_NOT_IN_NEW:
		case PROPOSAL_NOT_IN_SENT:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

	}

	@PostMapping("/api/v1/proposals/reject/{id}")
	public ResponseEntity<Object> rejectProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponseType response = proposalService.rejectProposalById(accountId, id);

		switch (response) {
		case PROPOSAL_NOT_IN_NEW:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PostMapping("/api/v1/proposals/revision/{id}")
	public ResponseEntity<Object> reviewProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id, String duration, String paymentMethod, String paymentValue) {

		ServiceResponseType response = proposalService.reviewProposal(accountId, id, duration, paymentMethod,
				paymentValue);

		switch (response) {
		case OK:
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/api/v1/proposals/accept/{id}")
	public ResponseEntity<Object> acceptProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponseType response = proposalService.acceptProposal(accountId, id);

		switch (response) {
		case OK:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
