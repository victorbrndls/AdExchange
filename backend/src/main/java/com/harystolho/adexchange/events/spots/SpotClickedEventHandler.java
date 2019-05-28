package com.harystolho.adexchange.events.spots;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spots.events.SpotClickedEvent;
import com.harystolho.adexchange.services.payment.SpotActionVerifier;

@Service
public class SpotClickedEventHandler extends AbstractSpotEventHandler implements Handler<SpotClickedEvent> {

	private SpotActionVerifier contractPaymentVerifier;

	private SpotClickedEventHandler(EventDispatcher eventDispatcher, SpotActionVerifier contractPaymentVerifier) {
		super(eventDispatcher);
		this.contractPaymentVerifier = contractPaymentVerifier;

		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		contractPaymentVerifier.verifySpotClick(event.getSpotRedirectId(), event.getTracker());
	}

}
