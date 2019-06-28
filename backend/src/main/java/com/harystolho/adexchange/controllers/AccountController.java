package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.controllers.models.BalanceWithdrawModel;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.BalanceService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.JsonResponse;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class AccountController {

	private AccountService accountService;
	private BalanceService balanceService;

	@Autowired
	public AccountController(AccountService accountService, BalanceService balanceService) {
		this.accountService = accountService;
		this.balanceService = balanceService;
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

	@PatchMapping(path = "/api/v1/account", params = "form=info")
	public ResponseEntity<Object> updateAccountInfo(@RequestAttribute("ae.accountId") String accountId, String name) {
		ServiceResponse<Account> response = accountService.updateAccountInfo(accountId, name);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		}
	}

	@PatchMapping(path = "/api/v1/account", params = "form=auth")
	public ResponseEntity<Object> updateAccountAuth(@RequestAttribute("ae.accountId") String accountId, String email,
			String password) {
		ServiceResponse<Account> response = accountService.updateAccountAuth(accountId, email, password);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		}
	}

	@GetMapping(path = "/api/v1/account", params = "fields=balance")
	public ResponseEntity<Object> getUserBalance(@RequestAttribute("ae.accountId") String accountId) {
		ServiceResponse<String> response = accountService.getAccountBalance(accountId);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(JsonResponse.of("balance", response.getReponse())
					.pair("lastUpdate", System.currentTimeMillis()).build());
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/api/v1/account/withdraw-balance")
	public ResponseEntity<Object> requestBalanceWithdraw(@RequestAttribute("ae.accountId") String accountId,
			String mode, String value, @RequestParam(required = false) String email,
			@RequestParam(required = false) String cpf, @RequestParam(required = false) String bankCode,
			@RequestParam(required = false) String accountAgency,
			@RequestParam(required = false) String accountNumber) {

		ServiceResponseType response = balanceService.requestBalanceWithdraw(new BalanceWithdrawModel()
				.setAccountId(accountId).setMode(mode).setValue(value).setEmail(email).setCpf(cpf).setBankCode(bankCode)
				.setAccountAgency(accountAgency).setAccountNumber(accountNumber));

		switch (response) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(null);
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
