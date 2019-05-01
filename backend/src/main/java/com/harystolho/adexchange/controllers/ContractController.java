package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Contract;
import com.harystolho.adexchange.services.ContractService;
import com.harystolho.adexchange.services.ServiceResponse;

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
	 * @return the contracts that belong to the account that made the request
	 */
	public ResponseEntity<Object> getContracts(@RequestAttribute("ae.accountId") String accountId) {

		ServiceResponse<List<Contract>> response = contractService.getContractsByAccountId(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

	@GetMapping("/api/v1/contracts/{id}")
	@CrossOrigin
	public ResponseEntity<Object> getContractById(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {

		ServiceResponse<Contract> response = contractService.getContractById(accountId, id);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

	@GetMapping("/api/v1/contracts/batch")
	@CrossOrigin
	/**
	 * @param accountId
	 * @param ids
	 * @param embed     [website, ad]
	 * @return
	 */
	public ResponseEntity<Object> getContractsById(@RequestAttribute("ae.accountId") String accountId, String ids,
			@RequestParam(defaultValue = "") String embed) {

		ServiceResponse<List<ObjectNode>> response = contractService.getContractsById(accountId, ids, embed);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

	@GetMapping(path = "/api/v1/contracts", params = { "owner" })
	@CrossOrigin
	/**
	 * @return Contracts for websites owned by the user
	 */
	public ResponseEntity<Object> getContractsForAccountWebsites(@RequestAttribute("ae.accountId") String accountId) {

		ServiceResponse<List<Contract>> response = contractService.getContractsForUserWebisites(accountId);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}
}
