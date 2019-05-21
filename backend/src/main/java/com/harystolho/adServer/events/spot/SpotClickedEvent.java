package com.harystolho.adServer.events.spot;

import com.harystolho.adServer.events.Event;

public class SpotClickedEvent implements Event {

	private final String spotRedirectId;

	public SpotClickedEvent(String spotRedirectId) {
		this.spotRedirectId = spotRedirectId;
	}

	public String getSpotRedirectId() {
		return spotRedirectId;
	}

	@Override
	public Class<? extends Event> getType() {
		return SpotClickedEvent.class;
	}

}
