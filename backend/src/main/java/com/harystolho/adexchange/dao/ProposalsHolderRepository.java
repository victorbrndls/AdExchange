package com.harystolho.adexchange.dao;

import com.harystolho.adexchange.models.ProposalsHolder;

public interface ProposalsHolderRepository {

	/**
	 * Saves the new proposals. Replaces the existing one
	 * 
	 * @param proposalsHolder
	 * @return
	 */
	ProposalsHolder save(ProposalsHolder proposalsHolder);

	ProposalsHolder getByAccountId(String accountId);

}