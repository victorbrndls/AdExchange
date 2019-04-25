package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.harystolho.adexchange.dao.ProposalRepository;
import com.harystolho.adexchange.models.Proposal;

public class ProposalRepositoryMemoryImpl implements ProposalRepository {

	private List<Proposal> proposals;

	public ProposalRepositoryMemoryImpl() {
		proposals = new ArrayList<>();
	}

	@Override
	public Proposal save(Proposal proposal) {
		proposal.setId(UUID.randomUUID().toString());

		proposals.add(proposal);

		return proposal;
	}

	@Override
	public Optional<Proposal> getById(String id) {
		return Optional.empty();
	}

	@Override
	public List<Proposal> getByAccountId(String accountId) {
		return new ArrayList<>();
	}

}
