package com.harystolho.adexchange.models.account;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
public class Account {

	private String id;
	private String email;
	private String password;

	private String fullName;
	private Balance balance;

	public Account() {
		this.balance = new Balance("0.00");
	}

	public Account(String email, String password) {
		this();
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}
}
