package com.harystolho.adexchange.dao.impl;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.AuthRepository;
import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AuthRespositoryMemoryImpl implements AuthRepository {

	@Override
	public Pair<RepositoryResponse, Account> saveAccount(Account account) {
		return Pair.of(RepositoryResponse.CREATED, null);
	}

}
