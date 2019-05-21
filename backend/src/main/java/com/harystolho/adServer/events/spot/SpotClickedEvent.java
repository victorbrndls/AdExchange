package com.harystolho.adServer.events.spot;

import com.harystolho.adServer.events.Event;
import com.harystolho.adServer.tracker.Tracker;

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
