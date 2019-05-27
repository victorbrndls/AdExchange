package com.harystolho.adexchange.events.spot;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spot.events.SpotUpdatedEvent;
import com.harystolho.adserver.services.AdModelService;

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
