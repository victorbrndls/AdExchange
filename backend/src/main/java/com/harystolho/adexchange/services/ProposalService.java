package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.dao.ProposalRepository;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Proposal.PaymentMethod;
import com.harystolho.adexchange.utils.Pair;

@Service
public class ProposalService {

	private ProposalRepository proposalRepository;

	private WebsiteService websiteService;
	private AdService adService;

	public ProposalService(ProposalRepository proposalRepository, WebsiteService websiteService, AdService adService) {
		this.proposalRepository = proposalRepository;
		this.websiteService = websiteService;
		this.adService = adService;
	}

	public Pair<ServiceResponse, List<Proposal>> getAccountProposals() {
		return Pair.of(ServiceResponse.OK, proposalRepository.getByAccountId(""));
	}

	public Pair<ServiceResponse, Proposal> createProposal(String websiteId, String adId, String duration,
			String paymentMethod, String paymentValue) {
		ServiceResponse validation = validateProposalFields(websiteId, adId, duration, paymentMethod, paymentValue);

		if (validation != null)
			return Pair.of(validation, null);

		Proposal proposal = new Proposal();
		proposal.setWebsiteId(websiteId);
		proposal.setAdId(adId);
		proposal.setDuration(Integer.parseInt(duration));
		proposal.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		proposal.setPaymentValue(paymentValue);

		Proposal saved = proposalRepository.save(proposal);

		return Pair.of(ServiceResponse.OK, saved);
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

			if (iDuration <= 0 || iDuration > 365)
				return ServiceResponse.INVALID_DURATION;
		} catch (Exception e) {
			return ServiceResponse.INVALID_DURATION;
		}

		// Payment Method
		if (paymentMethod != null && !(paymentMethod.equals("PAY_PER_CLICK") || paymentMethod.equals("PAY_PER_VIEW")))
			return ServiceResponse.INVALID_PAYMENT_METHOD;

		// Payment Value
		try {
			int occurences = StringUtils.countOccurrencesOf(paymentValue, ".");

			if (occurences > 1)
				return ServiceResponse.INVALID_PAYMENT_VALUE;

			double value = Double.parseDouble(paymentValue);

			if (value <= 0.0)
				return ServiceResponse.INVALID_PAYMENT_VALUE;

			if (paymentValue.contains("."))
				if (paymentValue.split("\\.")[1].length() > 2)
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
		return websiteId != null && websiteService.getWebsiteById(websiteId).getSecond() != null;
	}

	/**
	 * @param websiteId
	 * @return TRUE if the ad matched by the id exists
	 */
	private boolean adExists(String adId) {
		return adId != null && adService.getAdById(adId).getSecond() != null;
	}

}
