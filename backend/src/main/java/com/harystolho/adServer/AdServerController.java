package com.harystolho.adServer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.harystolho.adexchange.services.ServiceResponse;

@Controller
public class AdServerController {

	private AdServerService adServerService;

	@GetMapping("/serve/v1/ads")
	public ResponseEntity<Object> serveAds(HttpServletRequest req, String ids) {

		ServiceResponse<List<AdModel>> response = adServerService.getAds(ids);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response.getReponse());
	}

}
