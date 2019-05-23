package com.harystolho.adServer.events.account;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.harystolho.adServer.events.EventDispatcher;
import com.harystolho.adServer.events.Handler;
import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.account.Account;

@Service
public class AccountBalanceChangeHandler implements Handler<AccountBalanceChangedEvent> {

	private EventDispatcher eventDispatcher;
	private final Logger logger;

	public AccountBalanceChangeHandler(Logger logger, EventDispatcher eventDispatcher) {
		this.logger = logger;
		this.eventDispatcher = eventDispatcher;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(AccountBalanceChangedEvent.class, this);
	}

	@Override
	public void onEvent(AccountBalanceChangedEvent event) {
		Account acc = event.getAccount();

		logger.info("Updated account balance // accountId: [%s], old balance: [%s], new balance: [%s]", acc.getId(),
				event.getOldBalance().toString(), acc.getBalance().toString());
	}

}
