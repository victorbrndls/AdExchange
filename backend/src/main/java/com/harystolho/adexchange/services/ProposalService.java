package com.harystolho.adexchange.services;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.utils.Pair;

@Service
public class ProposalService {

	private WebsiteService websiteService;
	private AdService adService;

	public ProposalService(WebsiteService websiteService, AdService adService) {
		this.websiteService = websiteService;
		this.adService = adService;
	}

	public Pair<ServiceResponse, Proposal> createProposal(String websiteId, String adId, String duration,
			String paymentMethod, String paymentValue) {
		ServiceResponse validation = validateProposalFields(websiteId, adId, duration, paymentMethod, paymentValue);

		if (validation != null)
			return Pair.of(validation, null);

		return Pair.of(ServiceResponse.OK, null);
	}

	/**
	 * @param websiteId
	 * @param adId
	 * @param duration
	 * @param paymentMethod
	 * @param paymentValue
	 * @return <code>null</code> if the fields are valid for the proposal creation
	 *         or the corresponding error
	 */
	private ServiceResponse validateProposalFields(String websiteId, String adId, String duration, String paymentMethod,
			String paymentValue) {
		if (!websiteExists(websiteId))
			return ServiceResponse.INVALID_WEBSITE_ID;

		if (!adExists(adId))
			return ServiceResponse.INVALID_AD_ID;

		// Duration
		try {
			int iDuration = Integer.parseInt(duration);

			if (iDuration < 0 || iDuration > 365)
				return ServiceResponse.INVALID_DURATION;
		} catch (Exception e) {
			return ServiceResponse.INVALID_DURATION;
		}

		// Payment Method
		if (!(paymentMethod.equals("PAY_PER_CLICK") || paymentMethod.equals("PAY_PER_VIEW")))
			return ServiceResponse.INVALID_PAYMENT_METHOD;

		// Payment Value
		try {
			double value = Double.parseDouble(paymentValue);

			if (value < 0.0)
				return ServiceResponse.INVALID_PAYMENT_VALUE;

			if (paymentValue.contains("."))
				if (paymentMethod.split(".")[1].length() > 2)
					return ServiceResponse.INVALID_PAYMENT_VALUE;
		} catch (Exception e) {
			return ServiceResponse.INVALID_PAYMENT_VALUE;
		}

		return null;
	}

	/**
	 * @param websiteId
	 * @return TRUE if the website matched by the id exists
	 */
	private boolean websiteExists(String websiteId) {
		return websiteService.getWebsiteById(websiteId).getSecond() != null;
	}

	private boolean adExists(String adId) {
		return adService.getAdById(adId).getSecond() != null;
	}

}
