package com.harystolho.adexchange.services.payment;

import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.analytics.AnalyticsService;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Spot;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.services.SpotService;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adserver.AdModel;
import com.harystolho.adserver.services.UrlRedirecterService;
import com.harystolho.adserver.services.UrlRedirecterService.SpotData;
import com.harystolho.adserver.services.admodel.AdModelCacheService;
import com.harystolho.adserver.services.admodel.AdModelFactory.AdSource;
import com.harystolho.adserver.tracker.Tracker;
import com.harystolho.adserver.tracker.UserTrackerService;

@Service
public class SpotActionProcessor {

	private static final Logger logger = LogManager.getLogger();

	private final UrlRedirecterService urlRedirecterService;
	private final ContractPaymentService contractPaymentService;
	private final SpotService spotService;
	private final UserTrackerService userTrackerService;
	private final AnalyticsService analyticsService;
	private final AdModelCacheService adModelCacheService;

	public SpotActionProcessor(UrlRedirecterService urlRedirecterService, ContractPaymentService contractPaymentService,
			SpotService spotService, UserTrackerService userTrackerService, AnalyticsService analyticsService,
			AdModelCacheService adModelCacheService) {
		this.urlRedirecterService = urlRedirecterService;
		this.contractPaymentService = contractPaymentService;
		this.spotService = spotService;
		this.userTrackerService = userTrackerService;
		this.analyticsService = analyticsService;
		this.adModelCacheService = adModelCacheService;
	}

	/**
	 * Verify the user has not interacted with the spot before and issues a contract
	 * payment if applicable
	 * 
	 * @param event
	 */
	public void processSpotClick(String spotRedirectId, Tracker tracker) {
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

		verifyUserHasNotClickedContract(tracker, spot.getContractId());
	}

	public void processSpotView(String spotId, Tracker tracker) {
		AdModel model = adModelCacheService.get(spotId);

		if (model == null)
			return;

		if (model.getAdSource() != AdSource.CONTRACT)
			return;

		ServiceResponse<Spot> spotResponse = spotService.getSpot(AEUtils.ADMIN_ACCESS_ID, spotId, "");

		if (spotResponse.getErrorType() != ServiceResponseType.OK) {
			logger.error("AdModel spot id is not valid. spotId: [{}]", spotId);
			return;
		}

		Spot spot = spotResponse.getReponse();

		verifyUserHasNotViewedContract(tracker, spot.getContractId());

	}

	private void verifyUserHasNotViewedContract(Tracker tracker, String contractId) {
		verifyUserHasNotInteractedWithContract(tracker, "v_", contractId, analyticsService::incrementTotalViews,
				analyticsService::incrementUniqueViews);
	}

	private void verifyUserHasNotClickedContract(Tracker tracker, String contractId) {
		boolean hasInteracted = verifyUserHasNotInteractedWithContract(tracker, "c_", contractId,
				analyticsService::incrementTotalClicks, analyticsService::incrementUniqueClicks);

		if (!hasInteracted)
			// Issue payment to the website owner
			contractPaymentService.issueContractPayment(contractId,
					Arrays.asList(PaymentMethod.PAY_PER_CLICK, PaymentMethod.PAY_PER_VIEW));
	}

	/**
	 * @param hasInteractedFunction    function called if the user has interacted
	 *                                 with the contract before
	 * @param hasNotInteractedFunction function called if the user has NOT
	 *                                 interacted with the contract before
	 * 
	 * @return <code>true</code> if the user has interacted with the contract in the
	 *         past
	 */
	private boolean verifyUserHasNotInteractedWithContract(Tracker tracker, String prefix, String contractId,
			Consumer<String> hasInteractedFunction, Consumer<String> hasNotInteractedFunction) {
		if (userTrackerService.hasTrackerInteractedWith(tracker, prefix + contractId)) {
			hasInteractedFunction.accept(contractId);

			return true;
		} else {
			// Record that the user has interacted with the contract
			userTrackerService.interactTrackerWith(tracker, prefix + contractId);

			hasNotInteractedFunction.accept(contractId);

			return false;
		}
	}
}
