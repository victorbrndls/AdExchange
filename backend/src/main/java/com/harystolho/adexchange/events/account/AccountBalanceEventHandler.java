package com.harystolho.adexchange.events.account;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import com.harystolho.adexchange.log.Logger;
import com.harystolho.adServer.services.AdServerService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.log.Log.Identifier;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.models.account.Balance;

@Service
public class AccountBalanceEventHandler implements Handler<AccountBalanceChangedEvent> {

	private EventDispatcher eventDispatcher;
	private final Logger logger;

	private AdServerService adServerService;

	public AccountBalanceEventHandler(Logger logger, EventDispatcher eventDispatcher, AdServerService adServerService) {
		this.logger = logger;
		this.eventDispatcher = eventDispatcher;
		this.adServerService = adServerService;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(AccountBalanceChangedEvent.class, this);
	}

	@Override
	public void onEvent(AccountBalanceChangedEvent event) {
		Account acc = event.getAccount();

		logger.info(Identifier.ACCOUNT_BALANCE_CHANGED, "accountId: [%s], old balance: [%s], new balance: [%s]",
				acc.getId(), event.getOldBalance().toString(), acc.getBalance().toString());

		if (hasBalanceDecreased(acc.getBalance(), event.getOldBalance())) {

		}
	}

	private boolean hasBalanceDecreased(Balance newBalance, Balance oldBalance) {
		return newBalance.compare(oldBalance) == -1;
	}

}
