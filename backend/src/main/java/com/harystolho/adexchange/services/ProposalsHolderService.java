package com.harystolho.adexchange.services;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.ProposalsHolderRepository;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;

@Service
public class ProposalsHolderService {

	private static final Logger logger = LogManager.getLogger();

	private ProposalsHolderRepository proposalsHolderRepository;

	private AdService adService;
	private WebsiteService websiteService;

	@Autowired
	public ProposalsHolderService(ProposalsHolderRepository proposalsHolder, AdService adService,
			WebsiteService websiteService) {
		this.proposalsHolderRepository = proposalsHolder;
		this.adService = adService;
		this.websiteService = websiteService;
	}

	private void addNewProposalToAccount(String accountId, String proposalId) {
		ProposalsHolder holder = getProposalHolderByAccountId(accountId);

		holder.addNewProposal(proposalId);
		proposalsHolderRepository.save(holder);
	}

	private void addSentProposalToAccount(String accountId, String proposalId) {
		ProposalsHolder holder = getProposalHolderByAccountId(accountId);

		holder.addSentProposal(proposalId);
		proposalsHolderRepository.save(holder);
	}

	private void removeNewProposalFromAccount(String accountId, String proposalId) {
		proposalsHolderRepository.removeProposalFromNew(accountId, proposalId);
	}

	private void removeSentProposalFromAccount(String accountId, String proposalId) {
		proposalsHolderRepository.removeProposalFromSent(accountId, proposalId);
	}

	private ProposalsHolder createProposalsHolderForAccount(String accountId) {
		ProposalsHolder holder = new ProposalsHolder();
		holder.setAccountId(accountId);

		return proposalsHolderRepository.save(holder);
	}

	public void addProposal(Proposal proposal) {
		// The user that created the proposal also created the Ad in the proposal
		String senderId = getSenderIdUsingAdId(proposal.getAdId());

		// The proposal contains the website id, and it contains the creator's id
		String recieverId = getRecieverIdUsingWebsiteId(proposal.getWebsiteId());

		if (senderId.equals(recieverId)) {
			logger.info("User tried to create a proposal for his/her website. accountId:{}", senderId);
			return;
		}

		addSentProposalToAccount(senderId, proposal.getId());
		addNewProposalToAccount(recieverId, proposal.getId());
	}

	/**
	 * Deletes the proposal from the sender and reciever's proposals holder. This
	 * method deletes the proposal from the sender's perspective, if you wish to
	 * delete the proposal from the reciver's perspective call
	 * {@link #rejectProposal(Proposal)}
	 * 
	 * @param proposal
	 */
	public void removeProposal(Proposal proposal) {
		String senderId = getSenderIdUsingAdId(proposal.getAdId());
		String recieverId = getRecieverIdUsingWebsiteId(proposal.getWebsiteId());

		removeSentProposalFromAccount(senderId, proposal.getId());
		removeNewProposalFromAccount(recieverId, proposal.getId());
	}

	public void rejectProposal(Proposal proposal) {

	}

	public ProposalsHolder getProposalHolderByAccountId(String accountId) {
		ProposalsHolder ph = proposalsHolderRepository.getByAccountId(accountId);
		return ph != null ? ph : createProposalsHolderForAccount(accountId);
	}

	private String getSenderIdUsingAdId(String adId) {
		return adService.getAccountIdUsingAdId(adId);
	}

	private String getRecieverIdUsingWebsiteId(String websiteId) {
		return websiteService.getAccountIdUsingWebsiteId(websiteId);
	}

}
