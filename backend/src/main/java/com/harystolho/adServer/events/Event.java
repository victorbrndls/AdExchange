package com.harystolho.adServer.events;

public interface Event {

	Class<? extends Event> getType();
	
}
