package com.harystolho.adexchange.events.spots;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spots.events.SpotClickedEvent;
import com.harystolho.adexchange.services.payment.SpotActionProcessor;

@Service
public class SpotClickedEventHandler extends AbstractSpotEventHandler implements Handler<SpotClickedEvent> {

	private SpotActionProcessor contractPaymentVerifier;

	private SpotClickedEventHandler(EventDispatcher eventDispatcher, SpotActionProcessor contractPaymentVerifier) {
		super(eventDispatcher);
		this.contractPaymentVerifier = contractPaymentVerifier;

		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		contractPaymentVerifier.processSpotClick(event.getSpotRedirectId(), event.getTracker());
	}

}
