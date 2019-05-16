package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.JsonResponse;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class AccountController {

	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/api/v1/account/me")
	public ResponseEntity<Object> getUserAccount(@RequestAttribute("ae.accountId") String accountId) {
		ServiceResponse<Account> response = accountService.getAccountById(accountId);

		switch (response.getErrorType()) {
		case OK:
			Account acc = response.getReponse();
			ObjectNode node = JsonResponse.of("name", acc.getFullName()).pair("email", acc.getEmail()).build();
			return ResponseEntity.status(HttpStatus.OK).body(node);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

	}
}
