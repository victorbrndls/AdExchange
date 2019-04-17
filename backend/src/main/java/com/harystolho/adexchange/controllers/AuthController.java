package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.services.AuthService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.JsonResponse;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@RestController()
public class AuthController {

	private AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/auth/account")
	@CrossOrigin
	public ResponseEntity<Object> createAccount(@RequestParam("email") String email,
			@RequestParam("password") String password) {

		Pair<ServiceResponse, Nothing> response = authService.createAccount(email, password);

		switch (response.getFist()) {
		case INVALID_EMAIL:
		case EMAIL_ALREADY_EXISTS:
		case INVALID_PASSWORD:		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse().pair("error", response.getFist().toString()).build());
		default:
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		}

	}

}