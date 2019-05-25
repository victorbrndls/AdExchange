package com.harystolho.adexchange.events.contracts;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.contracts.events.ContractCreatedEvent;

public class ContractCreatedEventHandler extends AbstractContractEventHandler implements Handler<ContractCreatedEvent> {

	public ContractCreatedEventHandler(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}

	@Override
	public void onEvent(ContractCreatedEvent event) {
		
	}

}
