package com.harystolho.adexchange.events.proposals;

import com.harystolho.adexchange.events.AbstractEventHandler;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.services.NotificationService;

public class AbstractProposalEventHandler extends AbstractEventHandler {

	protected NotificationService notificationService;

	public AbstractProposalEventHandler(EventDispatcher eventDispatcher, NotificationService notificationService) {
		super(eventDispatcher);
		this.notificationService = notificationService;
	}

}
