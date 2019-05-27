package com.harystolho.adexchange.events.account;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.harystolho.adserver.services.SpotUpdater;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.log.Log.LogIdentifier;
import com.harystolho.adexchange.log.Logger;
import com.harystolho.adexchange.models.account.Account;

@Service
public class AccountBalanceEventHandler implements Handler<AccountBalanceChangedEvent> {

	private final EventDispatcher eventDispatcher;
	private final Logger logger;

	private final SpotUpdater contractUpdater;

	public AccountBalanceEventHandler(Logger logger, EventDispatcher eventDispatcher, SpotUpdater contractUpdater) {
		this.logger = logger;
		this.eventDispatcher = eventDispatcher;
		this.contractUpdater = contractUpdater;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(AccountBalanceChangedEvent.class, this);
	}

	@Override
	public void onEvent(AccountBalanceChangedEvent event) {
		Account acc = event.getAccount();

		logger.info(LogIdentifier.ACCOUNT_BALANCE_CHANGED, "accountId: [%s], old balance: [%s], new balance: [%s]",
				acc.getId(), event.getOldBalance().toString(), acc.getBalance().toString());

		if (event.hasBalanceDecreased()) {
			contractUpdater.updateSpotsAdvertisedByUser(acc.getId());
		}
	}

}
