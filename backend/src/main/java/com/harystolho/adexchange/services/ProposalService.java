package com.harystolho.adexchange.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.repositories.proposal.ProposalRepository;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.utils.Nothing;
import com.harystolho.adexchange.utils.Pair;

@Service
public class ProposalService {

	private ProposalRepository proposalRepository;
	private ProposalsHolderService proposalsHolderService;

	private WebsiteService websiteService;
	private AdService adService;
	private ContractService contractService;

	public ProposalService(ProposalRepository proposalRepository, ProposalsHolderService proposalsHolderService,
			WebsiteService websiteService, AdService adService, ContractService contractService) {
		this.proposalRepository = proposalRepository;
		this.proposalsHolderService = proposalsHolderService;
		this.websiteService = websiteService;
		this.adService = adService;
		this.contractService = contractService;
	}

	public Pair<ServiceResponse, ProposalsHolder> getProposalsByAccountId(String accountId) {
		return Pair.of(ServiceResponse.OK, proposalsHolderService.getProposalHolderByAccountId(accountId));
	}

	public Pair<ServiceResponse, Proposal> getProposalById(String accountId, String id) {
		return Pair.of(ServiceResponse.OK, proposalRepository.getById(id));
	}

	public Pair<ServiceResponse, List<Proposal>> getProposalsById(String proposalIds) {
		String[] proposalsIds = proposalIds.split(",");

		List<Proposal> proposals = new ArrayList<>();

		for (String id : proposalsIds) {
			Proposal prop = proposalRepository.getById(id);

			if (prop != null)
				proposals.add(prop);
		}

		return Pair.of(ServiceResponse.OK, proposals);
	}

	public Pair<ServiceResponse, Proposal> createProposal(String accountId, String websiteId, String adId,
			String duration, String paymentMethod, String paymentValue) {
		ServiceResponse validation = validateProposalFields(websiteId, adId, duration, paymentMethod, paymentValue);

		if (validation != ServiceResponse.OK)
			return Pair.of(validation, null);

		Proposal proposal = new Proposal();
		proposal.setCreatorAccountId(accountId);
		proposal.setWebsiteId(websiteId);
		proposal.setAdId(adId);
		proposal.setDuration(Integer.parseInt(duration));
		proposal.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		proposal.setPaymentValue(paymentValue);

		Proposal saved = proposalRepository.save(proposal);

		proposalsHolderService.addProposal(saved);

		return Pair.of(ServiceResponse.OK, saved);
	}

	public Pair<ServiceResponse, Nothing> deleteProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (proposal.isRejected()) {
			if (!proposalsHolderService.containsProposalInNew(accountId, proposal))
				return Pair.of(ServiceResponse.FAIL, null);
		} else {
			if (!proposalsHolderService.containsProposalInSent(accountId, proposal))
				return Pair.of(ServiceResponse.FAIL, null);
		}

		proposalRepository.deleteById(id);
		proposalsHolderService.removeProposal(proposal);

		return Pair.of(ServiceResponse.OK, null);
	}

	public Pair<ServiceResponse, Nothing> rejectProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (!proposalsHolderService.containsProposalInNew(accountId, proposal))
			return Pair.of(ServiceResponse.FAIL, null);

		proposalRepository.setRejected(id);
		proposalsHolderService.rejectProposal(proposal);

		return Pair.of(ServiceResponse.OK, null);
	}

	public Pair<ServiceResponse, Nothing> reviewProposal(String accountId, String id, String duration,
			String paymentMethod, String paymentValue) {
		if (!validateDuration(duration))
			return Pair.of(ServiceResponse.INVALID_DURATION, null);
		if (!validatePaymentMethod(paymentMethod))
			return Pair.of(ServiceResponse.INVALID_PAYMENT_METHOD, null);
		if (!validatePaymentValue(paymentValue))
			return Pair.of(ServiceResponse.INVALID_PAYMENT_VALUE, null);

		Proposal prop = proposalRepository.getById(id);

		if (prop == null)
			return Pair.of(ServiceResponse.FAIL, null);

		if (!proposalsHolderService.containsProposalInNew(accountId, prop))
			return Pair.of(ServiceResponse.FAIL, null);

		prop.setDuration(Integer.parseInt(duration));
		prop.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		prop.setPaymentValue(paymentValue);
		prop.setVersion(prop.getVersion() + 1);

		proposalRepository.save(prop);

		proposalsHolderService.reviewProposal(prop);

		return Pair.of(ServiceResponse.OK, null);
	}

	public Pair<ServiceResponse, Nothing> acceptProposal(String accountId, String id) {
		Proposal prop = proposalRepository.getById(id);

		if (!proposalsHolderService.containsProposalInNew(accountId, prop))
			return Pair.of(ServiceResponse.FAIL, null);

		// Only the website owner can accept the proposal
		if (!websiteService.accountOwnsWebsite(accountId, prop.getWebsiteId())) {
			return Pair.of(ServiceResponse.FAIL, null);
		}

		contractService.createContractFromProposal(prop);

		proposalsHolderService.acceptProposal(prop);

		proposalRepository.deleteById(id);

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
		if (!validateDuration(duration))
			return ServiceResponse.INVALID_DURATION;

		// Payment Method
		if (!validatePaymentMethod(paymentMethod))
			return ServiceResponse.INVALID_PAYMENT_METHOD;

		// Payment Value
		if (!validatePaymentValue(paymentValue))
			return ServiceResponse.INVALID_PAYMENT_VALUE;

		return ServiceResponse.OK;
	}

	private boolean validateDuration(String duration) {
		try {
			int iDuration = Integer.parseInt(duration);

			if (iDuration <= 0 || iDuration > 365)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean validatePaymentMethod(String method) {
		if (method == null)
			return false;

		if (!(method.equals("PAY_PER_CLICK") || method.equals("PAY_PER_VIEW")))
			return false;

		return true;
	}

	private boolean validatePaymentValue(String pValue) {
		try {
			int occurences = StringUtils.countOccurrencesOf(pValue, ".");

			if (occurences > 1)
				return false;

			if (pValue.contains("."))
				if (pValue.split("\\.")[1].length() > 2) // More than 2 places after the '.'
					return false;

			double value = Double.parseDouble(pValue);

			if (value <= 0.0)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
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
