package com.harystolho.adexchange.controllers.models;

public class BalanceWithdrawModel {

	private String accountId;
	private String mode;
	private String value;

	private String email;

	private String cpf;
	private String bankCode;
	private String accountAgency;
	private String accountNumber;

	public BalanceWithdrawModel setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public BalanceWithdrawModel setMode(String mode) {
		this.mode = mode;
		return this;
	}

	public BalanceWithdrawModel setValue(String value) {
		this.value = value;
		return this;
	}

	public BalanceWithdrawModel setEmail(String email) {
		this.email = email;
		return this;
	}

	public BalanceWithdrawModel setCpf(String cpf) {
		this.cpf = cpf;
		return this;
	}

	public BalanceWithdrawModel setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}

	public BalanceWithdrawModel setAccountAgency(String accountAgency) {
		this.accountAgency = accountAgency;
		return this;
	}

	public BalanceWithdrawModel setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}

	/* GETTERS */
	public String getAccountId() {
		return accountId;
	}

	public String getMode() {
		return mode;
	}

	public String getValue() {
		return value;
	}

	public String getEmail() {
		return email;
	}

	public String getCpf() {
		return cpf;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getAccountAgency() {
		return accountAgency;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

}
