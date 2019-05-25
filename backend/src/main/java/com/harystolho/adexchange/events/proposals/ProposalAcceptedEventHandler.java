package com.harystolho.adexchange.events.proposals;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.proposals.events.ProposalAcceptedEvent;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.services.ContractService;
import com.harystolho.adexchange.services.NotificationService;

public class ProposalAcceptedEventHandler extends AbstractProposalEventHandler
		implements Handler<ProposalAcceptedEvent> {

	private final ContractService contractService;

	public ProposalAcceptedEventHandler(EventDispatcher eventDispatcher, NotificationService notificationService,
			ContractService contractService) {
		super(eventDispatcher, notificationService);
		this.contractService = contractService;
		
		eventDispatcher.registerHandler(ProposalAcceptedEvent.class, this);
	}

	@Override
	public void onEvent(ProposalAcceptedEvent event) {
		Proposal proposal = event.getProposal();

		contractService.createContractFromProposal(proposal, proposal.getProposerId(), proposal.getProposeeId());

		notificationService.emitAcceptedProposalNotification(proposal);
	}

}
