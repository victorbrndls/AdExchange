package com.harystolho.adexchange.events.accounts;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.models.account.Balance;

public class AccountBalanceChangedEvent implements Event {

	private final Account account;
	private final Balance oldBalance;

	public AccountBalanceChangedEvent(Account account, Balance oldBalance) {
		this.account = account;
		this.oldBalance = oldBalance;
	}

	public Account getAccount() {
		return account;
	}

	public Balance getOldBalance() {
		return oldBalance;
	}

	public boolean hasBalanceDecreased() {
		return account.getBalance().compare(oldBalance) == -1;
	}

}
