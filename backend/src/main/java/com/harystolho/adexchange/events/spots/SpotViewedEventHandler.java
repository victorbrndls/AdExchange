package com.harystolho.adexchange.events.spots;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spots.events.SpotViewedEvent;
import com.harystolho.adexchange.services.payment.SpotActionVerifier;

@Service
public class SpotViewedEventHandler extends AbstractSpotEventHandler implements Handler<SpotViewedEvent> {

	private SpotActionVerifier spotActionVerifier;

	private SpotViewedEventHandler(EventDispatcher eventDispatcher, SpotActionVerifier contractPaymentVerifier) {
		super(eventDispatcher);
		this.spotActionVerifier = contractPaymentVerifier;

		eventDispatcher.registerHandler(SpotViewedEvent.class, this);
	}

	@Override
	public void onEvent(SpotViewedEvent event) {
		spotActionVerifier.verifySpotView(event.getSpotId(), event.getTracker());
	}

}
