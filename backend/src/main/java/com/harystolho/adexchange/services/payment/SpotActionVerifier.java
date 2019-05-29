package com.harystolho.adexchange.services.payment;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.analytics.AnalyticsService;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.services.AdModelFactory.AdSource;
import com.harystolho.adserver.services.UrlRedirecterService.SpotData;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;

/**
 * Verifies whether the spot click/view was valid, and issues a payment in that
 * case
 * 
 * @author Harystolho
 *
 */
@Service
public class SpotActionVerifier {

	// Used to avoid collision in the userTrackerService
	private static final String INTERACTOR_PREFIX = "c_";

	private static final Logger logger = LogManager.getLogger();

	private UrlRedirecterService urlRedirecterService;
	private ContractPaymentService contractPaymentService;
	private SpotService spotService;
	private UserTrackerService userTrackerService;
	private AnalyticsService analyticsService;

	public SpotActionVerifier(UrlRedirecterService urlRedirecterService, ContractPaymentService contractPaymentService,
			SpotService spotService, UserTrackerService userTrackerService, AnalyticsService analyticsService) {
		this.urlRedirecterService = urlRedirecterService;
		this.contractPaymentService = contractPaymentService;
		this.spotService = spotService;
		this.userTrackerService = userTrackerService;
		this.analyticsService = analyticsService;
	}

	/**
	 * Verify the user has not interacted with the spot before and issues a contract
	 * payment if applicable
	 * 
	 * @param event
	 */
	public void verifySpotClick(String spotRedirectId, Tracker tracker) {
		ServiceResponse<SpotData> response = urlRedirecterService.getSpotDataUsingRedirectId(spotRedirectId);

		if (response.getErrorType() != ServiceResponseType.OK)
			return; // This should never happen

		SpotData data = response.getReponse();

		if (data.getAdSource() != AdSource.CONTRACT)
			return; // If the Ad wasn't created from a contract, payment shouldn't happen

		ServiceResponse<Spot> spotResponse = spotService.getSpot(AEUtils.ADMIN_ACCESS_ID, data.getSpotId(), "");

		if (spotResponse.getErrorType() != ServiceResponseType.OK) {
			logger.error("AdModel redirect url is not linked to a Spot. redirectId: [{}]", spotRedirectId);
			return;
		}

		Spot spot = spotResponse.getReponse();

		verifyUserHasNotInteractedWithContract(tracker, spot.getContractId());
	}

	private void verifyUserHasNotInteractedWithContract(Tracker tracker, String contractId) {
		if (userTrackerService.hasTrackerInteractedWith(tracker, INTERACTOR_PREFIX + contractId)) {
			// User has clicked the spot in the past
			analyticsService.incrementTotalClicks(contractId);
			return;
		}

		// Record that user has clicked the spot
		userTrackerService.interactTrackerWith(tracker, INTERACTOR_PREFIX + contractId);

		analyticsService.incrementUniqueClicks(contractId);

		// Issue payment to the website owner
		contractPaymentService.issueContractPayment(contractId,
				Arrays.asList(PaymentMethod.PAY_PER_CLICK, PaymentMethod.PAY_PER_VIEW));
	}

}
