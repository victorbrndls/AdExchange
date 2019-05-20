package com.harystolho.adServer.events;

public class SpotClickedEvent implements Event {

	@Override
	public Class<? extends Event> getType() {
		return SpotClickedEvent.class;
	}

}
