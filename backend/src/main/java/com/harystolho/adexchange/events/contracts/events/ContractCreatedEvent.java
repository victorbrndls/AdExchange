package com.harystolho.adexchange.events.contracts.events;

import com.harystolho.adexchange.events.Event;
import com.harystolho.adexchange.models.Contract;

public class ContractCreatedEvent implements Event {

	private final Contract contract;

	public ContractCreatedEvent(Contract contract) {
		this.contract = contract;
	}

	public Contract getContract() {
		return contract;
	}

	@Override
	public Class<? extends Event> getType() {
		return ContractCreatedEvent.class;
	}

}
