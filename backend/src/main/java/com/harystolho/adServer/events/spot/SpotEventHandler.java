package com.harystolho.adServer.events.spot;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.harystolho.adServer.events.EventDispatcher;
import com.harystolho.adServer.events.Handler;

@Service
public class SpotEventHandler implements Handler<SpotClickedEvent> {

	private EventDispatcher eventDispatcher;

	private SpotEventHandler(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		System.out.println(event.getSpotRedirectId());
	}

}
