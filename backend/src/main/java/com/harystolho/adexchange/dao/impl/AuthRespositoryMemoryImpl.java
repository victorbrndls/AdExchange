package com.harystolho.adexchange.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.dao.AuthRepository;
import com.harystolho.adexchange.dao.RepositoryResponse;
import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@Service
public class AuthRespositoryMemoryImpl implements AuthRepository {

	private List<Account> accounts;

	public AuthRespositoryMemoryImpl() {
		accounts = new ArrayList<Account>();
	}

	@Override
	public Pair<RepositoryResponse, Account> saveAccount(Account account) {
		accounts.add(account);
		
		account.setId(UUID.randomUUID().toString());
		return Pair.of(RepositoryResponse.CREATED, account);
	}

	@Override
	public Account getAccountByEmail(String email) {
		Optional<Account> optional = accounts.stream().filter((account) -> account.getEmail().equalsIgnoreCase(email))
				.findFirst();

		if (optional.isPresent())
			return optional.get();

		return null;
	}

}
