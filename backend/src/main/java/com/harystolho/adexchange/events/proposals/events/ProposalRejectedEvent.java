package com.harystolho.adexchange.events.proposals.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.Proposal;

public class ProposalRejectedEvent implements Event {

	private final Proposal proposal;
	private String rejectorId;

	public ProposalRejectedEvent(Proposal proposal, String rejectorId) {
		this.proposal = proposal;
		this.rejectorId = rejectorId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public String getRejectorId() {
		return rejectorId;
	}

	@Override
	public Class<? extends Event> getType() {
		return ProposalRejectedEvent.class;
	}

}
