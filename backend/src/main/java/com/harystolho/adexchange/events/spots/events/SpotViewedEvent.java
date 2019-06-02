package com.harystolho.adexchange.events.spots.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adserver.tracker.Tracker;

public class SpotViewedEvent implements Event {

	private final String spotId;
	private final Tracker tracker;

	public SpotViewedEvent(String spotId, Tracker tracker) {
		this.spotId = spotId;
		this.tracker = tracker;
	}

	public String getSpotId() {
		return spotId;
	}

	public Tracker getTracker() {
		return tracker;
	}

}
