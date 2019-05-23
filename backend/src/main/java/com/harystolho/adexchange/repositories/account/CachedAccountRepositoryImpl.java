package com.harystolho.adexchange.repositories.account;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.harystolho.adServer.services.CacheService;
import com.harystolho.adexchange.models.account.Account;

@Service(value = "cachedAccountRepository")
public class CachedAccountRepositoryImpl implements AccountRepository {

	private AccountRepository accountRepository;
	private CacheService<Account> cacheService;

	@Autowired
	private CachedAccountRepositoryImpl(@Qualifier("accountRepository") AccountRepository accountRepository,
			CacheService<Account> cacheService) {
		this.accountRepository = accountRepository;
		this.cacheService = cacheService;
	}

	@Override
	public Account save(Account account) {
		if (cacheService.contains(account.getId())) {
			cacheService.evict(account.getId()); // Remove old version

			cacheService.store(account.getId(), account, Duration.ofMinutes(1)); // Store new version
		}

		return accountRepository.save(account);
	}

	@Override
	public Account getById(String accountId) {
		Account account = cacheService.get(accountId); // Try to get account from cache

		if (account == null) {
			account = accountRepository.getById(accountId); // If it is not present get from database

			if (account != null)
				cacheService.store(accountId, account); // If it is present in the database, cache it for later
		}

		return account;
	}

	@Override
	public Account getByEmail(String email) {
		return accountRepository.getByEmail(email);
	}

}
