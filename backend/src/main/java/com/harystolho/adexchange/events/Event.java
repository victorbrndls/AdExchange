package com.harystolho.adexchange.events;

public interface Event {

	Class<? extends Event> getType();
	
}
