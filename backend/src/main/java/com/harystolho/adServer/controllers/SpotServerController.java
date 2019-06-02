package com.harystolho.adserver.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.spots.events.SpotViewedEvent;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.services.TrackableRequestService;
import com.harystolho.adserver.services.admodel.AdModelService;
import com.harystolho.adserver.tracker.Tracker;

@Controller
@CrossOrigin
public class SpotServerController {

	private AdModelService spotServerService;
	private EventDispatcher eventDispatcher;
	private TrackableRequestService trackableRequestService;

	@Autowired
	private SpotServerController(AdModelService spotServerService, EventDispatcher eventDispatcher,
			TrackableRequestService trackableRequestService) {
		this.spotServerService = spotServerService;
		this.eventDispatcher = eventDispatcher;
		this.trackableRequestService = trackableRequestService;
	}

	@GetMapping("/serve/v1/spots")
	public ResponseEntity<Object> serveSpots(HttpServletRequest req, HttpServletResponse res, String ids) {
		Tracker tracker = trackableRequestService.addTrackerToRequest(req, res);

		ServiceResponse<List<AdModel>> response = spotServerService.getSpots(ids);

		List<AdModel> models = response.getReponse();

		models.forEach(model -> eventDispatcher.dispatch(new SpotViewedEvent(model.getSpotId(), tracker)));

		return ResponseEntity.status(HttpStatus.OK).body(models);
	}

}
