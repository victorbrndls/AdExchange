package com.harystolho.adServer.events.spot;

import com.harystolho.adServer.events.Event;

public class SpotClickedEvent implements Event {

	private final String spotRedirectId;
	private final String tracker;

	public SpotClickedEvent(String spotRedirectId, String tracker) {
		this.spotRedirectId = spotRedirectId;
		this.tracker = tracker;
	}

	public String getSpotRedirectId() {
		return spotRedirectId;
	}

	public String getTracker() {
		return tracker;
	}

	@Override
	public Class<? extends Event> getType() {
		return SpotClickedEvent.class;
	}

}
