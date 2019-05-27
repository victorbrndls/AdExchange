package com.harystolho.adexchange.events.spots.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.Spot;

public class SpotUpdatedEvent implements Event {

	private final Spot spot;

	public SpotUpdatedEvent(Spot spot) {
		this.spot = spot;
	}

	public Spot getSpot() {
		return spot;
	}

}
