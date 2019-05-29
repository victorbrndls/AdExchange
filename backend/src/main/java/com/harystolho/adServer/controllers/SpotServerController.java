package com.harystolho.adserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.services.AdModelService;

@Controller
@CrossOrigin
public class SpotServerController {

	private AdModelService spotServerService;

	@Autowired
	private SpotServerController(AdModelService spotServerService) {
		this.spotServerService = spotServerService;
	}

	@GetMapping("/serve/v1/spots")
	public ResponseEntity<Object> serveSpots(String ids) {

		ServiceResponse<List<AdModel>> response = spotServerService.getSpots(ids);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

}
