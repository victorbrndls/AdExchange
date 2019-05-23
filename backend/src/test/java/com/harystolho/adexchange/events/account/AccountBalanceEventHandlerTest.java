package com.harystolho.adexchange.events.account;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.log.Logger;

public class AccountBalanceEventHandlerTest {

	@InjectMocks
	AccountBalanceEventHandler accountBalanceEventHandler;

	@Mock
	EventDispatcher eventDispatcher;
	@Mock
	Logger logger;

}
