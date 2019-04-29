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
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.services.ContractService;
import com.harystolho.adexchange.services.ProposalService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@RestController
public class ContractController {

	private ContractService contractService;

	@Autowired
	public ContractController(ContractService contractService) {
		this.contractService = contractService;
	}

	@GetMapping("/api/v1/contracts/me")
	@CrossOrigin
	/**
	 * @return the proposals that belong to the account that made the request
	 */
	public ResponseEntity<Object> getContracts(@RequestAttribute("ae.accountId") String accountId) {

		Pair<ServiceResponse, List<Contract>> response = contractService.getContractsByAccountId(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
	}

	@GetMapping("/api/v1/contract/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getContractById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		Pair<ServiceResponse, Contract> response = contractService.getContractById(accountId, id);

		switch (response.getFist()) {
		case FAIL:		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getSecond());
		}
	}
}
