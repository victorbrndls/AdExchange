package com.harystolho.adServer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.harystolho.adexchange.services.ServiceResponse;

@Controller
@CrossOrigin
public class SpotServerController {

	private SpotServerService spotServerService;

	@Autowired
	private SpotServerController(SpotServerService spotServerService) {
		this.spotServerService = spotServerService;
	}

	@GetMapping("/serve/v1/spots")
	public ResponseEntity<Object> serveSpots(String ids) {

		ServiceResponse<List<AdModel>> response = spotServerService.getSpots(ids);

		return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
	}

}
