package com.harystolho.adexchange.repositories.proposalsHolder;

import java.util.List;

import com.harystolho.adexchange.models.ProposalsHolder;

public interface ProposalsHolderRepository {

	ProposalsHolder save(ProposalsHolder proposalsHolder);

	ProposalsHolder getByAccountId(String accountId);

	void addProposalToSent(String accountId, String proposalId);

	void addProposalToNew(String accountId, String proposalId);

	void removeProposalFromSent(String accountId, String proposalId);

	void removeProposalFromNew(String accountId, String proposalId);

	List<String> getNewProposalsByAccountId(String accountId);

	List<String> getSentProposalsByAccountId(String accountId);
}