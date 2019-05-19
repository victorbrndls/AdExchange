package com.harystolho.adexchange.repositories.account;

import com.harystolho.adexchange.models.account.Account;

public interface AccountRepository {

	public Account save(Account account);
	
	public Account getById(String accountId);
	
	public Account getByEmail(String email);
	
}
