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
