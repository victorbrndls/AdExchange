package com.harystolho.adexchange.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthController {

	@PostMapping("/auth/account")
	@CrossOrigin
	public ResponseEntity<Object> createAccount(){
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}
