package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.models.UserData;
import com.harystolho.adexchange.repositories.userData.UserDataRepository;

/**
 * Don't forget to call {@link #createUserDataIfNonExistent(String)} before
 * adding something to the database
 * 
 * @author Harystolho
 *
 */
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
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);

		return ph.getNewProposals();
	}

	public List<String> getSentProposalsByAccountId(String accountId) {
		createUserDataIfNonExistent(accountId);

		ProposalsHolder ph = getProposalsHolderByAccoundId(accountId);

		return ph.getSentProposals();
	}

	public ProposalsHolder getProposalsHolderByAccoundId(String accountId) {
		createUserDataIfNonExistent(accountId);

		return userDataRepository.getProposalsHolderByAccountId(accountId);
	}

	private void createUserDataIfNonExistent(String accountId) {
		if (!userDataRepository.exists(accountId)) {
			UserData ud = new UserData();

			ud.setAccountId(accountId);
			ud.getProposalsHolder().setAccountId(accountId);

			userDataRepository.save(ud);
		}
	}

	private List<String> saveContracts(String accountId, List<String> contracts) {
		createUserDataIfNonExistent(accountId);

		return userDataRepository.saveContractsByAccountId(accountId, contracts);
	}

	public void addContract(String accountId, String id) {
		createUserDataIfNonExistent(accountId);

		List<String> contracts = userDataRepository.getContractsByAccountId(accountId);

		contracts.add(id);

		saveContracts(accountId, contracts);
	}

	public List<String> getContractsByAccountId(String accountId) {
		createUserDataIfNonExistent(accountId);

		return userDataRepository.getContractsByAccountId(accountId);
	}
}
