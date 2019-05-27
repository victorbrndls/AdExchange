package com.harystolho.adexchange.events.spots;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.services.AdModelFactory.AdSource;
import com.harystolho.adserver.services.UrlRedirecterService.SpotData;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;
import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.Handler;
import com.harystolho.adexchange.events.spots.events.SpotClickedEvent;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ContractPaymentService;
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
public class SpotClickedEventHandler extends AbstractSpotEventHandler implements Handler<SpotClickedEvent> {

	// Used to avoid collision in the userTrackerService
	private static final String INTERACTOR_PREFIX = "c_";

	private static final Logger logger = LogManager.getLogger();

	private UrlRedirecterService urlRedirecterService;
	private ContractPaymentService contractPaymentService;
	private SpotService spotService;
	private UserTrackerService userTrackerService;

	private SpotClickedEventHandler(EventDispatcher eventDispatcher, UrlRedirecterService urlRedirecterService,
			ContractPaymentService contractPaymentService, SpotService spotService,
			UserTrackerService userTrackerService) {
		super(eventDispatcher);
		this.urlRedirecterService = urlRedirecterService;
		this.contractPaymentService = contractPaymentService;
		this.spotService = spotService;
		this.userTrackerService = userTrackerService;

		eventDispatcher.registerHandler(SpotClickedEvent.class, this);
	}

	@Override
	public void onEvent(SpotClickedEvent event) {
		contractPaymentVerifier(event);
	}

	/**
	 * Verify the user has not interacted with the clicked spot and issues a
	 * contract payment if applicable
	 * 
	 * @param event
	 */
	private void contractPaymentVerifier(SpotClickedEvent event) {
		ServiceResponse<SpotData> response = urlRedirecterService.getSpotDataUsingRedirectId(event.getSpotRedirectId());

		if (response.getErrorType() != ServiceResponseType.OK)
			return; // This should never happen

		SpotData data = response.getReponse();

		if (data.getAdSource() != AdSource.CONTRACT)
			return; // If the Ad wasn't created from a contract, payment shouldn't happen

		ServiceResponse<Spot> spotResponse = spotService.getSpot(AEUtils.ADMIN_ACCESS_ID, data.getSpotId(), "");

		if (spotResponse.getErrorType() != ServiceResponseType.OK) {
			logger.error("AdModel redirect url is not linked to a Spot. redirectId: [{}]", event.getSpotRedirectId());
			return;
		}

		Spot spot = spotResponse.getReponse();

		verifyUserHasNotInteractedWithContract(event.getTracker(), spot.getContractId());
	}

	private void verifyUserHasNotInteractedWithContract(Tracker tracker, String contractId) {
		if (userTrackerService.hasTrackerInteractedWith(tracker, INTERACTOR_PREFIX + contractId))
			return; // User has clicked the spot in the past, do nothing

		// Record that user has clicked the spot
		userTrackerService.interactTrackerWith(tracker, INTERACTOR_PREFIX + contractId);

		// Issue payment to the website owner
		contractPaymentService.issueContractPayment(contractId,
				Arrays.asList(PaymentMethod.PAY_PER_CLICK, PaymentMethod.PAY_PER_VIEW));
	}

}
