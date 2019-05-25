package com.harystolho.adexchange.events.proposals;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.proposals.events.ProposalReviewedEvent;
import com.harystolho.adexchange.services.NotificationService;

public class ProposalReviewedEventHandler extends AbstractProposalEventHandler
		implements Handler<ProposalReviewedEvent> {

	public ProposalReviewedEventHandler(EventDispatcher eventDispatcher, NotificationService notificationService) {
		super(eventDispatcher, notificationService);
		eventDispatcher.registerHandler(ProposalReviewedEvent.class, this);
	}

	@Override
	public void onEvent(ProposalReviewedEvent event) {
		notificationService.emitReviewedProposalNotification(event.getProposal(), event.getReviewerId());
	}

}
