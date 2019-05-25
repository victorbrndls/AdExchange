package com.harystolho.adexchange.events.proposals;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.proposals.events.ProposalCreatedEvent;
import com.harystolho.adexchange.services.NotificationService;

@Service
public class ProposalCreatedEventHandler extends AbstractProposalEventHandler implements Handler<ProposalCreatedEvent> {

	public ProposalCreatedEventHandler(EventDispatcher eventDispatcher, NotificationService notificationService) {
		super(eventDispatcher, notificationService);
		eventDispatcher.registerHandler(ProposalCreatedEvent.class, this);
	}

	@Override
	public void onEvent(ProposalCreatedEvent event) {
		notificationService.emitNewProposalNotification(event.getProposal());
	}

}
