package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.services.AccountService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;

@RestController()
@CrossOrigin(origins = AEUtils.corsOrigin)
public class AuthController {

	private AccountService authService;

	@Autowired
	public AuthController(AccountService authService) {
		this.authService = authService;
	}

	@PostMapping("/auth/account")
	public ResponseEntity<Object> createAccount(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		ServiceResponse<Account> response = authService.createOrUpdateAccount(null, email, password);

		switch (response.getErrorType()) {
		case INVALID_EMAIL:
		case EMAIL_ALREADY_EXISTS:
		case INVALID_PASSWORD:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		}
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Object> login(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		ServiceResponse<String> response = authService.login(email, password);

		switch (response.getErrorType()) {
		case FAIL:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorType());
		default:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		}

	}

}
