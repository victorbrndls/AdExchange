package com.harystolho.adexchange.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.information.GlobalInformant;

@RestController
public class GlobelInformantController {

	private GlobalInformant globalInformant;

	@Autowired
	public GlobelInformantController(GlobalInformant globalInformant) {
		this.globalInformant = globalInformant;
	}

	@GetMapping("/info")
	public ResponseEntity<Object> getInformation() {
		return ResponseEntity.status(HttpStatus.OK).body(globalInformant.visitAll());
	}
}
