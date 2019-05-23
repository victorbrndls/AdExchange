package com.harystolho.adexchange.events.spot;

import com.harystolho.adServer.tracker.Tracker;
import com.harystolho.adexchange.events.Event;

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

	@Override
	public Class<? extends Event> getType() {
		return SpotClickedEvent.class;
	}

}
