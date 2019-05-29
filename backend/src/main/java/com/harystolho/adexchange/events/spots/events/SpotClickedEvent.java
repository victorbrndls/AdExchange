package com.harystolho.adexchange.events.spots.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adserver.tracker.Tracker;

public class SpotClickedEvent implements Event {

	private final String spotRedirectId;
	private final Tracker tracker;

	public SpotClickedEvent(String spotRedirectId, Tracker tracker) {
		this.spotRedirectId = spotRedirectId;
		this.tracker = tracker;
	}

	public String getSpotRedirectId() {
		return spotRedirectId;
	}

	public Tracker getTracker() {
		return tracker;
	}

}
