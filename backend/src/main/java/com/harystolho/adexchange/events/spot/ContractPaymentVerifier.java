package com.harystolho.adexchange.events.spot;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adServer.services.ContractPaymentService;
import com.harystolho.adServer.services.UrlRedirecterService;
import com.harystolho.adServer.tracker.Tracker;
import com.harystolho.adServer.tracker.UserTrackerService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;

/**
 * If the user has not clicked the spot before, make a request payment to the
 * website owner. If the user has clicked the spot before nothing happens
 * 
 * @author Harystolho
 *
 */
@Service
public class ContractPaymentVerifier implements Handler<SpotClickedEvent> {

	// Used to avoid collision in the userTrackerService
	private static final String INTERACTOR_PREFIX = "c_";

	private static final Logger logger = LogManager.getLogger();

	private EventDispatcher eventDispatcher;
	private UrlRedirecterService urlRedirecterService;
	private ContractPaymentService contractPaymentService;
	private SpotService spotService;
	private UserTrackerService userTrackerService;

	private ContractPaymentVerifier(EventDispatcher eventDispatcher, UrlRedirecterService urlRedirecterService,
			ContractPaymentService contractPaymentService, SpotService spotService,
			UserTrackerService userTrackerService) {
		this.eventDispatcher = eventDispatcher;
		this.urlRedirecterService = urlRedirecterService;
		this.contractPaymentService = contractPaymentService;
		this.spotService = spotService;
		this.userTrackerService = userTrackerService;
	}

	@PostConstruct
	private void postConstruct() {
		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		ServiceResponse<String> response = urlRedirecterService.getSpotIdUsingRedirectId(event.getSpotRedirectId());

		if (response.getErrorType() != ServiceResponseType.OK)
			return; // This should never happen

		ServiceResponse<Spot> spotResponse = spotService.getSpot(AEUtils.ADMIN_ACESS_ID, response.getReponse(), "");

		if (spotResponse.getErrorType() != ServiceResponseType.OK) {
			logger.error("AdModel redirect url is not linked to a Spot. redirectId: [{}]", event.getSpotRedirectId());
			return;
		}

		Spot spot = spotResponse.getReponse();

		if (spot.getContractId() == null || spot.getContractId().equals("-1"))
			// There is no contract in the spot, so there is no need to pay the website
			// owner
			return;

		// TODO make sure the ad owner has money to pay the ad
		
		verifyUserHasNotInteractedWithContract(event.getTracker(), spot.getContractId());
	}

	private void verifyUserHasNotInteractedWithContract(Tracker tracker, String contractId) {
		if (userTrackerService.hasTrackerInteractedWith(tracker, INTERACTOR_PREFIX + contractId))
			return; // User has clicked the spot in the past, do nothing

		// Record that user has clicked the spot
		userTrackerService.interactTrackerWith(tracker, INTERACTOR_PREFIX + contractId);

		// Issue payment to the website owner
		contractPaymentService.issueContractPayment(contractId);
	}

}
