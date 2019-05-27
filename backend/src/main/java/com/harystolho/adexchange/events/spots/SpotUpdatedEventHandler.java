package com.harystolho.adexchange.events.spots;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spots.events.SpotUpdatedEvent;
import com.harystolho.adserver.services.AdModelService;

@Service
public class SpotUpdatedEventHandler extends AbstractSpotEventHandler implements Handler<SpotUpdatedEvent> {

	private AdModelService adModelService;

	public SpotUpdatedEventHandler(EventDispatcher eventDispatcher, AdModelService adModelService) {
		super(eventDispatcher);
		this.adModelService = adModelService;

		eventDispatcher.registerHandler(SpotUpdatedEvent.class, this);
	}

	@Override
	public void onEvent(SpotUpdatedEvent event) {
		adModelService.updateSpot(event.getSpot());
	}

}
