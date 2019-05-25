package com.harystolho.adexchange.events;

public class AbstractEventHandler {

	protected EventDispatcher eventDispatcher;

	public AbstractEventHandler(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	
}
