package com.harystolho.adexchange.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.impl.ProposalsHolderRepositoryImpl;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;

@Service
public class ProposalsHolderService {

	private ProposalsHolderRepositoryImpl proposalsHolder;

	private AdService adService;
	private WebsiteService websiteService;

	@Autowired
	public ProposalsHolderService(ProposalsHolderRepositoryImpl proposalsHolder, AdService adService,
			WebsiteService websiteService) {
		this.proposalsHolder = proposalsHolder;
		this.adService = adService;
		this.websiteService = websiteService;
	}

	private void addNewProposalToAccount(String accountId, String proposalId) {
		ProposalsHolder holder = getProposalHolderByAccountId(accountId);

		holder.addNewProposal(proposalId);
		proposalsHolder.save(holder);
	}

	private void addSentProposalToAccount(String accountId, String proposalId) {
		ProposalsHolder holder = getProposalHolderByAccountId(accountId);

		holder.addSentProposal(proposalId);
		proposalsHolder.save(holder);
	}

	private void removeNewProposalFromAccount(String accountId, String proposalId) {

	}

	private void removeSentProposalFromAccount(String accountId, String proposalId) {

	}

	private ProposalsHolder createProposalsHolderForAccount(String accountId) {
		ProposalsHolder holder = new ProposalsHolder();
		holder.setAccountId(accountId);

		proposalsHolder.save(holder);

		return holder;
	}

	public void addProposal(Proposal proposal) {
		// The user that created the proposal also created the Ad in the proposal
		String senderId = getSenderIdUsingAdId(proposal.getAdId());

		// The proposal contains the website id, and it contains the creator's id
		String recieverId = getRecieverIdUsingWebsiteId(proposal.getWebsiteId());

		addSentProposalToAccount(senderId, proposal.getId());
		addNewProposalToAccount(recieverId, proposal.getId());
	}

	private ProposalsHolder getProposalHolderByAccountId(String accountId) {
		return proposalsHolder.getByAccountId(accountId).orElse(createProposalsHolderForAccount(accountId));
	}

	private String getSenderIdUsingAdId(String adId) {
		return adService.getAccountIdUsingAdId(adId);
	}

	private String getRecieverIdUsingWebsiteId(String websiteId) {
		return websiteService.getAccountIdUsingWebsiteId(websiteId);
	}

}
