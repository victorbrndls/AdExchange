package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.models.UserData;
import com.harystolho.adexchange.repositories.userData.UserDataRepository;

@Service
public class UserDataService {

	private UserDataRepository userDataRepository;

	private UserDataService(UserDataRepository userDataRepository) {
		this.userDataRepository = userDataRepository;
	}

	public ProposalsHolder saveProposalsHolder(ProposalsHolder proposalsHolder) {
		createUserDataIfNonExistent(proposalsHolder.getAccountId());

		return userDataRepository.saveProposalsHolder(proposalsHolder);
	}

	public void addNewProposalToPH(String accountId, String proposalId) {
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);
		ph.addNewProposal(proposalId);

		saveProposalsHolder(ph);
	}

	public void addSentProposalToPH(String accountId, String proposalId) {
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);
		ph.addSentProposal(proposalId);

		saveProposalsHolder(ph);
	}

	public void removeNewProposalFromPH(String accountId, String proposalId) {
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);
		ph.removeNewProposal(proposalId);

		saveProposalsHolder(ph);
	}

	public void removeSentProposalFromPH(String accountId, String proposalId) {
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);
		ph.removeSentProposal(proposalId);

		saveProposalsHolder(ph);
	}

	public List<String> getNewProposalsByAccountId(String accountId) {
		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);

		return ph.getNewProposals();
	}

	public List<String> getSentProposalsByAccountId(String accountId) {
		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);

		return ph.getSentProposals();
	}

	public ProposalsHolder getProposalsHolderByAccoundId(String accountId) {
		return userDataRepository.getProposalsHolderByAccountId(accountId);
	}

	private void createUserDataIfNonExistent(String accountId) {
		if (!userDataRepository.exists(accountId)) {
			UserData ud = new UserData();

			ud.setAccountId(accountId);
			userDataRepository.save(ud);
		}
	}

}
