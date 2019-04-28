package com.harystolho.adexchange.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.repositories.ProposalsHolderRepository;
import com.harystolho.adexchange.utils.Pair;

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

	public void removeProposal(Proposal proposal) {
		String adOwner = getSenderIdUsingAdId(proposal.getAdId());
		String websiteOwner = getRecieverIdUsingWebsiteId(proposal.getWebsiteId());

		String remover = adOwner;
		String other = websiteOwner;

		if (proposal.isRejected()) {
			if (containsProposalInNew(websiteOwner, proposal)) {
				remover = websiteOwner;
				other = adOwner;
			}

			removeNewProposalFromAccount(remover, proposal.getId());
		} else {
			if (containsProposalInSent(websiteOwner, proposal)) {
				remover = websiteOwner;
				other = adOwner;
			}

			removeSentProposalFromAccount(remover, proposal.getId());
			removeNewProposalFromAccount(other, proposal.getId());
		}
	}

	public void rejectProposal(Proposal proposal) {
		Pair<String, String> pair = getExecutorAndOther(proposal);

		String deleter = pair.getFist();
		String other = pair.getSecond();

		// Remove the proposal from new for the account that rejected it
		removeNewProposalFromAccount(deleter, proposal.getId());

		// Remove the proposal from sent for the account that was the other user
		// involved
		removeSentProposalFromAccount(other, proposal.getId());

		// Add the proposal to new for the other user, the proposal
		// 'rejected' field must be set to true
		addNewProposalToAccount(other, proposal.getId());
	}

	public void reviewProposal(Proposal proposal) {
		Pair<String, String> pair = getExecutorAndOther(proposal);

		swapProposal(pair.getFist(), pair.getSecond(), proposal);
	}

	private Pair<String, String> getExecutorAndOther(Proposal p) {
		String adOwner = getSenderIdUsingAdId(p.getAdId());
		String websiteOwner = getRecieverIdUsingWebsiteId(p.getWebsiteId());

		String executor = websiteOwner;
		String other = adOwner;

		if (containsProposalInNew(adOwner, p)) {
			executor = adOwner;
			other = websiteOwner;
		}

		return Pair.of(executor, other);
	}

	/**
	 * Swaps the proposal in the {@link ProposalsHolder}
	 * 
	 * @param inNew    the proposal is in 'new' for this account
	 * @param inSent   the proposal is in 'sent' for this account
	 * @param proposal
	 */
	private void swapProposal(String inNew, String inSent, Proposal proposal) {
		removeNewProposalFromAccount(inNew, proposal.getId());
		addSentProposalToAccount(inNew, proposal.getId());

		removeSentProposalFromAccount(inSent, proposal.getId());
		addNewProposalToAccount(inSent, proposal.getId());
	}

	public boolean containsProposalInNew(String accountId, Proposal proposal) {
		List<String> proposals = phRepository.getNewProposalsByAccountId(accountId);

		return proposals.stream().anyMatch(propId -> propId.equals(proposal.getId()));
	}

	public boolean containsProposalInSent(String accountId, Proposal proposal) {
		List<String> proposals = phRepository.getSentProposalsByAccountId(accountId);

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