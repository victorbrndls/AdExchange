package com.harystolho.adexchange.events.proposals.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.Proposal;

public class ProposalCreatedEvent implements Event {

	private final Proposal proposal;

	public ProposalCreatedEvent(Proposal proposal) {
		this.proposal = proposal;
	}

	public Proposal getProposal() {
		return proposal;
	}

	@Override
	public Class<? extends Event> getType() {
		return ProposalCreatedEvent.class;
	}

}
