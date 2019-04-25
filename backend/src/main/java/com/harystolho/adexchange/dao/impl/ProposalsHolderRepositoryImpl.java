package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.harystolho.adexchange.models.ProposalsHolder;

@Service
public class ProposalsHolderRepositoryImpl {

	private List<ProposalsHolder> holders;

	public ProposalsHolderRepositoryImpl() {
		holders = new ArrayList<>();
	}

	/**
	 * Saves the new proposals. Replaces the existing one
	 * @param proposalsHolder
	 * @return
	 */
	public ProposalsHolder save(ProposalsHolder proposalsHolder) {
		proposalsHolder.setId(UUID.randomUUID().toString());

		return proposalsHolder;
	}

	public Optional<ProposalsHolder> getByAccountId(String accountId) {
		return holders.stream().filter(holder -> holder.getAccountId().equals(holder.getAccountId())).findFirst();
	}

}
