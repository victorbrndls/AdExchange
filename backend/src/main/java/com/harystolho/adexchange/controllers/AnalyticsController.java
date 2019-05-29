package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.analytics.AnalyticModel;
import com.harystolho.adexchange.analytics.AnalyticsService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.utils.AEUtils;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class AnalyticsController {

	private AnalyticsService analyticsService;

	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@GetMapping("/api/v1/analytics/{id}")
	public ResponseEntity<Object> getContractAnalytics(@RequestAttribute("ae.accountId") String accountId,
			@PathVariable String id) {
		ServiceResponse<List<AnalyticModel>> response = analyticsService.getAnalyticsForModel(accountId, id);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		default:
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.getFullMessage());
		}
	}

}
