package com.harystolho.adexchange.events.contracts;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.harystolho.adServer.services.ContractPaymentService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.contracts.events.ContractCreatedEvent;
import com.harystolho.adexchange.models.Contract.PaymentMethod;

@Service
public class ContractCreatedEventHandler extends AbstractContractEventHandler implements Handler<ContractCreatedEvent> {

	private ContractPaymentService contractPaymentService;

	public ContractCreatedEventHandler(EventDispatcher eventDispatcher, ContractPaymentService contractPaymentService) {
		super(eventDispatcher);
		this.contractPaymentService = contractPaymentService;

		eventDispatcher.registerHandler(ContractCreatedEvent.class, this);
	}

	@Override
	public void onEvent(ContractCreatedEvent event) {
		contractPaymentVerifier(event);
	}

	/**
	 * Issues a possible contract payment
	 * 
	 * @param event
	 */
	private void contractPaymentVerifier(ContractCreatedEvent event) {
		contractPaymentService.issueContractPayment(event.getContract().getId(), Arrays.asList(PaymentMethod.PAY_ONCE));
	}

}
