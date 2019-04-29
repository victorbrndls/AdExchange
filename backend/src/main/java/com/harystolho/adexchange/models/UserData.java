package com.harystolho.adexchange.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("userData")
public class UserData {

	private String id;
	private String accountId;

	private ProposalsHolder proposalsHolder;
	private List<String> contracts;

	public UserData() {
		setProposalsHolder(new ProposalsHolder());
		contracts = new ArrayList<>();
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

	public ProposalsHolder getProposalsHolder() {
		return proposalsHolder;
	}

	public void setProposalsHolder(ProposalsHolder proposalsHolder) {
		this.proposalsHolder = proposalsHolder;
	}

	public List<String> getContracts() {
		return contracts;
	}

	public void setContracts(List<String> contracts) {
		this.contracts = contracts;
	}

}
