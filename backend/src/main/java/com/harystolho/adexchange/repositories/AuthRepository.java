package com.harystolho.adexchange.repositories;

import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

public interface AuthRepository {

	public Account save(Account account);
	
	public Account getByEmail(String email);
	
}
