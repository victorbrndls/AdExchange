package com.harystolho.adexchange.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Stores which proposals were sent by the user and which proposals the user can
 * review
 * 
 * @author Harystolho
 *
 */
@Document("proposalsHolders")
public class ProposalsHolder {

	private String id;
	private String accountId;

	// The user can review/edit these ones
	private List<String> newProposals;

	// The use sent these ones for somebody else to edit
	private List<String> sentProposals;

	public ProposalsHolder() {
		newProposals = new ArrayList<>();
		sentProposals = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public List<String> getNewProposals() {
		return newProposals;
	}

	public void setNewProposals(List<String> newProposals) {
		this.newProposals = newProposals;
	}

	public void addNewProposal(String proposalId) {
		this.newProposals.add(proposalId);
	}

	public List<String> getSentProposals() {
		return sentProposals;
	}

	public void setSentProposals(List<String> sentProposals) {
		this.sentProposals = sentProposals;
	}

	public void addSentProposal(String proposalId) {
		this.sentProposals.add(proposalId);
	}

	public void removeNewProposal(String proposalId) {
		this.newProposals.remove(proposalId);
	}
	
	public void removeSentProposal(String proposalId) {
		this.sentProposals.remove(proposalId);
	}
}
