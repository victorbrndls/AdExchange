package com.harystolho.adexchange.repositories.account;

import com.harystolho.adexchange.models.Account;

public interface AccountRepository {

	public Account save(Account account);
	
	public Account getByEmail(String email);
	
}
