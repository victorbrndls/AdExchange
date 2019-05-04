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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.services.ProposalService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.Nothing;

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
	public ResponseEntity<Object> getProposals(@RequestAttribute("ae.accountId") String accountId) {

		ServiceResponse<ProposalsHolder> response = proposalService.getProposalsByAccountId(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

	@GetMapping("/api/v1/proposals/{id}")
	public ResponseEntity<Object> getProposalById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Proposal> response = proposalService.getProposalById(accountId, id);

		ObjectNode node = (ObjectNode) new ObjectMapper().valueToTree(response.getReponse());
		node.put("owner", response.getReponse().getCreatorAccountId().equals(accountId));

		return ResponseEntity.status(HttpStatus.OK).body(node);
	}

	@GetMapping("/api/v1/proposals/batch")
	/**
	 * @param ids list of proposals ids separated by comma
	 * @return
	 */
	public ResponseEntity<Object> getProposalsById(String ids) {
		ServiceResponse<List<Proposal>> response = proposalService.getProposalsById(ids);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

	@PostMapping("/api/v1/proposals")
	public ResponseEntity<Object> createProposal(@RequestAttribute("ae.accountId") String accountId, String websiteId,
			String adId, String duration, String paymentMethod, String paymentValue) {

		ServiceResponse<Proposal> response = proposalService.createProposal(accountId, websiteId, adId, duration,
				paymentMethod, paymentValue);

		switch (response.getErrorType()) {
		case INVALID_WEBSITE_ID:
		case INVALID_AD_ID:
		case INVALID_DURATION:
		case INVALID_PAYMENT_METHOD:
		case INVALID_PAYMENT_VALUE:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType().toString());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getFullMessage());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(response.getReponse());
		}
	}

	@DeleteMapping("/api/v1/proposals/{id}")
	public ResponseEntity<Object> deleteProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Nothing> response = proposalService.deleteProposalById(accountId, id);

		switch (response.getErrorType()) {
		case PROPOSAL_NOT_IN_NEW:
		case PROPOSAL_NOT_IN_SENT:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType().toString());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

	}

	@PostMapping("/api/v1/proposals/reject/{id}")
	public ResponseEntity<Object> rejectProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Nothing> response = proposalService.rejectProposalById(accountId, id);

		switch (response.getErrorType()) {
		case PROPOSAL_NOT_IN_NEW:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType().toString());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PostMapping("/api/v1/proposals/revision/{id}")
	public ResponseEntity<Object> reviewProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id, String duration, String paymentMethod, String paymentValue) {

		ServiceResponse<Nothing> response = proposalService.reviewProposal(accountId, id, duration, paymentMethod,
				paymentValue);

		switch (response.getErrorType()) {
		case INVALID_DURATION:
		case INVALID_PAYMENT_METHOD:
		case INVALID_PAYMENT_VALUE:
		case PROPOSAL_NOT_IN_NEW:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType().toString());
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@PostMapping("/api/v1/proposals/accept/{id}")
	public ResponseEntity<Object> acceptProposal(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Nothing> response = proposalService.acceptProposal(accountId, id);

		switch (response.getErrorType()) {
		case PROPOSAL_NOT_IN_NEW:
		case UNAUTHORIZED:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType().toString());
		default:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
}
