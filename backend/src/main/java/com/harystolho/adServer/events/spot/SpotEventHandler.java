package com.harystolho.adServer.events.spot;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.harystolho.adServer.events.EventDispatcher;
import com.harystolho.adServer.events.Handler;
import com.harystolho.adServer.services.ContractPaymentService;
import com.harystolho.adServer.services.UrlRedirecterService;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;

@Service
public class SpotEventHandler implements Handler<SpotClickedEvent> {

	private EventDispatcher eventDispatcher;
	private UrlRedirecterService urlRedirecterService;
	private ContractPaymentService contractPaymentService;
	private SpotService spotService;

	private SpotEventHandler(EventDispatcher eventDispatcher, UrlRedirecterService urlRedirecterService,
			ContractPaymentService contractPaymentService, SpotService spotService) {
		this.eventDispatcher = eventDispatcher;
		this.urlRedirecterService = urlRedirecterService;
		this.contractPaymentService = contractPaymentService;
		this.spotService = spotService;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		ServiceResponse<String> response = urlRedirecterService.getSpotIdUsingRedirectId(event.getSpotRedirectId());

		if (response.getErrorType() != ServiceResponseType.OK)
			return; // TODO what do do now ?

		ServiceResponse<Spot> spotResponse = spotService.getSpot(AEUtils.ADMIN_ACESS_ID, response.getReponse(), "");

		if (spotResponse.getErrorType() != ServiceResponseType.OK)
			return; // TODO what do do now ?

		Spot spot = spotResponse.getReponse();

		if (spot.getContractId() == null || spot.getContractId().equals("-1"))
			return; // There is no contract in the spot, so there is no need to pay the advertiser
		
		contractPaymentService.issueContractPayment(spot.getContractId());
	}

}
