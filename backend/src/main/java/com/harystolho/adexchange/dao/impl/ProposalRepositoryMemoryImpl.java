package com.harystolho.adexchange.dao.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalRepository;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Proposal.PaymentMethod;

@Service
public class ProposalRepositoryMemoryImpl implements ProposalRepository {

	private List<Proposal> proposals;

	public ProposalRepositoryMemoryImpl() {
		proposals = new ArrayList<>();

		Proposal p1 = new Proposal();
		p1.setWebsiteId("27792eaa-abc3-46c0-88c7-82464673bf90");
		p1.setAdId("631a94d4-d85c-46a2-aaac-a2eafa572b6a");
		p1.setDuration(7);
		p1.setPaymentMethod(PaymentMethod.PAY_PER_CLICK);
		p1.setPaymentValue("1.41");
		p1.setCreationDate(Date.from(Instant.now().minus(7, ChronoUnit.DAYS)));

		save(p1);
	}

	@Override
	public Proposal save(Proposal proposal) {
		proposal.setId(UUID.randomUUID().toString());

		proposals.add(proposal);

		return proposal;
	}

	@Override
	public Optional<Proposal> getById(String id) {
		return proposals.stream().filter(prop -> prop.getId().equals(id)).findFirst();
	}

	@Override
	public List<Proposal> getByAccountId(String accountId) {
		return proposals;
	}

}
