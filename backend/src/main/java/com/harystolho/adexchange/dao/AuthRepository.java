package com.harystolho.adexchange.dao;

import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

public interface AuthRepository {

	public Pair<RepositoryResponse, Account> saveAccount(Account account);
	
}
