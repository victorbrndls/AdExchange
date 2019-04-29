package com.harystolho.adexchange.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.repositories.proposal.ProposalRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.ProposalsHolder;
import com.harystolho.adexchange.utils.Nothing;

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

	public ServiceResponse<ProposalsHolder> getProposalsByAccountId(String accountId) {
		return ServiceResponse.ok(proposalsHolderService.getProposalHolderByAccountId(accountId));
	}

	public ServiceResponse<Proposal> getProposalById(String accountId, String id) {
		return ServiceResponse.ok(proposalRepository.getById(id));
	}

	public ServiceResponse<List<Proposal>> getProposalsById(String proposalIds) {
		String[] proposalsIds = proposalIds.split(",");

		List<Proposal> proposals = new ArrayList<>();

		for (String id : proposalsIds) {
			Proposal prop = proposalRepository.getById(id);

			if (prop != null)
				proposals.add(prop);
		}

		return ServiceResponse.ok(proposals);
	}

	public ServiceResponse<Proposal> createProposal(String accountId, String websiteId, String adId, String duration,
			String paymentMethod, String paymentValue) {
		ServiceResponseType validation = validateProposalFields(websiteId, adId, duration, paymentMethod, paymentValue);

		if (validation != ServiceResponseType.OK)
			return ServiceResponse.error(validation);

		Proposal proposal = new Proposal();
		proposal.setCreatorAccountId(accountId);
		proposal.setWebsiteId(websiteId);
		proposal.setAdId(adId);
		proposal.setDuration(Integer.parseInt(duration));
		proposal.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		proposal.setPaymentValue(paymentValue);

		Proposal saved = proposalRepository.save(proposal);

		proposalsHolderService.addProposal(saved);

		return ServiceResponse.ok(saved);
	}

	public ServiceResponse<Nothing> deleteProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (proposal.isRejected()) {
			if (!proposalsHolderService.containsProposalInNew(accountId, proposal))
				return ServiceResponse.proposalNotInNew();
		} else {
			if (!proposalsHolderService.containsProposalInSent(accountId, proposal))
				return ServiceResponse.proposalNotInSent();
		}

		proposalRepository.deleteById(id);
		proposalsHolderService.removeProposal(proposal);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> rejectProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (!proposalsHolderService.containsProposalInNew(accountId, proposal))
			return ServiceResponse.proposalNotInNew();

		proposalRepository.setRejected(id);
		proposalsHolderService.rejectProposal(proposal);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> reviewProposal(String accountId, String id, String duration, String paymentMethod,
			String paymentValue) {
		if (!validateDuration(duration))
			return ServiceResponse.error(ServiceResponseType.INVALID_DURATION);
		if (!validatePaymentMethod(paymentMethod))
			return ServiceResponse.error(ServiceResponseType.INVALID_PAYMENT_METHOD);
		if (!validatePaymentValue(paymentValue))
			return ServiceResponse.error(ServiceResponseType.INVALID_PAYMENT_VALUE);

		Proposal prop = proposalRepository.getById(id);

		if (prop == null)
			return ServiceResponse.fail("Can't find a Proposal using the given id");

		if (!proposalsHolderService.containsProposalInNew(accountId, prop))
			return ServiceResponse.proposalNotInNew();

		prop.setDuration(Integer.parseInt(duration));
		prop.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		prop.setPaymentValue(paymentValue);
		prop.setVersion(prop.getVersion() + 1);

		proposalRepository.save(prop);

		proposalsHolderService.reviewProposal(prop);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> acceptProposal(String accountId, String id) {
		Proposal prop = proposalRepository.getById(id);

		if (!proposalsHolderService.containsProposalInNew(accountId, prop))
			return ServiceResponse.proposalNotInNew();

		if (!websiteService.accountOwnsWebsite(accountId, prop.getWebsiteId())) {
			return ServiceResponse.unauthorized();
		}

		String creator = adService.getAccountIdUsingAdId(prop.getAdId());
		String acceptor = websiteService.getAccountIdUsingWebsiteId(prop.getWebsiteId());

		contractService.createContractFromProposal(prop, creator, acceptor);

		proposalsHolderService.acceptProposal(prop);

		proposalRepository.deleteById(id);

		return ServiceResponse.ok(null);
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
	private ServiceResponseType validateProposalFields(String websiteId, String adId, String duration,
			String paymentMethod, String paymentValue) {
		if (!websiteExists(websiteId))
			return ServiceResponseType.INVALID_WEBSITE_ID;

		if (!adExists(adId))
			return ServiceResponseType.INVALID_AD_ID;

		// Duration
		if (!validateDuration(duration))
			return ServiceResponseType.INVALID_DURATION;

		// Payment Method
		if (!validatePaymentMethod(paymentMethod))
			return ServiceResponseType.INVALID_PAYMENT_METHOD;

		// Payment Value
		if (!validatePaymentValue(paymentValue))
			return ServiceResponseType.INVALID_PAYMENT_VALUE;

		return ServiceResponseType.OK;
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
		return websiteId != null && websiteService.getWebsiteById(websiteId).getReponse() != null;
	}

	/**
	 * @param websiteId
	 * @return TRUE if the ad matched by the id exists
	 */
	private boolean adExists(String adId) {
		return adId != null && adService.getAdById(adId).getReponse() != null;
	}

}
