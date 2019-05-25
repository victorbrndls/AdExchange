package com.harystolho.adexchange.events.proposals.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.Proposal;

public class ProposalReviewedEvent implements Event {

	private final Proposal proposal;
	private final String reviewerId;

	public ProposalReviewedEvent(Proposal proposal, String reviewerId) {
		this.proposal = proposal;
		this.reviewerId = reviewerId;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	@Override
	public Class<? extends Event> getType() {
		return ProposalReviewedEvent.class;
	}

}
