package com.harystolho.adexchange.repositories.auth;

import com.harystolho.adexchange.models.Account;

public interface AuthRepository {

	public Account save(Account account);
	
	public Account getByEmail(String email);
	
}
