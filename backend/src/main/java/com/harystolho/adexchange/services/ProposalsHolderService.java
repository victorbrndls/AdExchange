package com.harystolho.adexchange.services;

import java.util.List;
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

	private ProposalsHolderRepository phRepository;

	private AdService adService;
	private WebsiteService websiteService;

	@Autowired
	public ProposalsHolderService(ProposalsHolderRepository proposalsHolder, AdService adService,
			WebsiteService websiteService) {
		this.phRepository = proposalsHolder;
		this.adService = adService;
		this.websiteService = websiteService;
	}

	private void addNewProposalToAccount(String accountId, String proposalId) {
		if (phRepository.getByAccountId(accountId) == null) {
			createProposalsHolderForAccount(accountId);
		}

		phRepository.addProposalToNew(accountId, proposalId);
	}

	private void addSentProposalToAccount(String accountId, String proposalId) {
		if (phRepository.getByAccountId(accountId) == null) {
			createProposalsHolderForAccount(accountId);
		}

		phRepository.addProposalToSent(accountId, proposalId);
	}

	private void removeNewProposalFromAccount(String accountId, String proposalId) {
		phRepository.removeProposalFromNew(accountId, proposalId);
	}

	private void removeSentProposalFromAccount(String accountId, String proposalId) {
		phRepository.removeProposalFromSent(accountId, proposalId);
	}

	private ProposalsHolder createProposalsHolderForAccount(String accountId) {
		ProposalsHolder holder = new ProposalsHolder();
		holder.setAccountId(accountId);

		return phRepository.save(holder);
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

		if (!proposal.isRejected()) {
			removeSentProposalFromAccount(senderId, proposal.getId());
			removeNewProposalFromAccount(recieverId, proposal.getId());
		} else {
			removeNewProposalFromAccount(senderId, proposal.getId());
		}
	}

	public void rejectProposal(Proposal proposal) {
		String senderId = getSenderIdUsingAdId(proposal.getAdId());
		String recieverId = getRecieverIdUsingWebsiteId(proposal.getWebsiteId());

		String deleter = recieverId;
		String other = deleter.equals(recieverId) ? senderId : recieverId;

		// Remove the proposal from new for the account that rejected it
		removeNewProposalFromAccount(deleter, proposal.getId());

		// Remove the proposal from sent for the account that was the other user
		// involved
		removeSentProposalFromAccount(other, proposal.getId());

		// Add the proposal to new for the other user, the proposal
		// 'rejected' field must be set to true
		addNewProposalToAccount(other, proposal.getId());
	}

	public boolean containsProposalInNew(String accountId, Proposal proposal) {
		List<String> proposals = phRepository.getNewProposalsByAccountId(accountId);

		return proposals.stream().anyMatch(propId -> propId.equals(proposal.getId()));
	}

	public ProposalsHolder getProposalHolderByAccountId(String accountId) {
		ProposalsHolder ph = phRepository.getByAccountId(accountId);
		return ph != null ? ph : createProposalsHolderForAccount(accountId);
	}

	private String getSenderIdUsingAdId(String adId) {
		return adService.getAccountIdUsingAdId(adId);
	}

	private String getRecieverIdUsingWebsiteId(String websiteId) {
		return websiteService.getAccountIdUsingWebsiteId(websiteId);
	}

}