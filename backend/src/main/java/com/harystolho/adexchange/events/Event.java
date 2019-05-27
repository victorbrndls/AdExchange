package com.harystolho.adexchange.events;

public interface Event {

	default Class<? extends Event> getType() {
		return getClass();
	}

}
