package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.services.AuthService;

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
		
		
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
