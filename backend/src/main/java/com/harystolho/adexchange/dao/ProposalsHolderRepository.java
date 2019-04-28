package com.harystolho.adexchange.dao;

import com.harystolho.adexchange.models.ProposalsHolder;

public interface ProposalsHolderRepository {

	ProposalsHolder save(ProposalsHolder proposalsHolder);

	ProposalsHolder getByAccountId(String accountId);

	void addProposalToSent(String accountId, String proposalId);

	void addProposalToNew(String accountId, String proposalId);

	void removeProposalFromSent(String accountId, String proposalId);

	void removeProposalFromNew(String accountId, String proposalId);
}