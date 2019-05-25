package com.harystolho.adexchange.events.proposals;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.proposals.events.ProposalRejectedEvent;
import com.harystolho.adexchange.services.NotificationService;

@Service
public class ProposalRejectedEventHandler extends AbstractProposalEventHandler
		implements Handler<ProposalRejectedEvent> {

	public ProposalRejectedEventHandler(EventDispatcher eventDispatcher, NotificationService notificationService) {
		super(eventDispatcher, notificationService);
		eventDispatcher.registerHandler(ProposalRejectedEvent.class, this);
	}

	@Override
	public void onEvent(ProposalRejectedEvent event) {
		notificationService.emitRejectedProposalNotification(event.getProposal(), event.getRejectorId());
	}

}
