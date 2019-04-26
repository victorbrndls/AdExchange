package com.harystolho.adexchange.dao.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalsHolderRepository;
import com.harystolho.adexchange.models.ProposalsHolder;

@Service
public class ProposalsHolderRepoMemoryImpl implements ProposalsHolderRepository {

	private Set<ProposalsHolder> holders;

	public ProposalsHolderRepoMemoryImpl() {
		holders = new HashSet<>();
		
		ProposalsHolder p1 = new ProposalsHolder();
		p1.setAccountId("b3179c4bbe464e9ab7e7e76aa15fc4d2");
		p1.addSentProposal("1b1f7e4d-08a7-4007-816b-d89af5803dce");
		p1.addSentProposal("79c4bbe468a7-4007-816b4");
		p1.addNewProposal("1234567");
		p1.addNewProposal("abc");
		p1.addNewProposal("1b1f7e4d-08a7-4007-816b-d89af5803dce");
		save(p1);
	}

	@Override
	public ProposalsHolder save(ProposalsHolder proposalsHolder) {
		Iterator<ProposalsHolder> it = holders.iterator();

		boolean contains = false;

		while (it.hasNext()) {
			ProposalsHolder next = it.next();

			if (next.getId().equals(proposalsHolder.getId())) {
				contains = true;
				break;
			}
		}

		if (!contains) {
			proposalsHolder.setId(UUID.randomUUID().toString());
			holders.add(proposalsHolder);
		}

		return proposalsHolder;
	}

	@Override
	public Optional<ProposalsHolder> getByAccountId(String accountId) {
		return holders.stream().filter(holder -> holder.getAccountId().equals(accountId)).findFirst();
	}

}
