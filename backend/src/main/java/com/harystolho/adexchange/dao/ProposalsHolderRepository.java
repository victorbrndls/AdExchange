package com.harystolho.adexchange.dao;

import com.harystolho.adexchange.models.ProposalsHolder;

public interface ProposalsHolderRepository {

	ProposalsHolder save(ProposalsHolder proposalsHolder);

	ProposalsHolder getByAccountId(String accountId);

}