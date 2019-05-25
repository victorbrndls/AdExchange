package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.events.EventDispatcher;
import com.harystolho.adexchange.events.proposals.events.ProposalAcceptedEvent;
import com.harystolho.adexchange.events.proposals.events.ProposalCreatedEvent;
import com.harystolho.adexchange.events.proposals.events.ProposalRejectedEvent;
import com.harystolho.adexchange.events.proposals.events.ProposalReviewedEvent;
import com.harystolho.adexchange.models.Contract.PaymentMethod;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.repositories.proposal.ProposalRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.Nothing;

@Service
public class ProposalService {

	private ProposalRepository proposalRepository;

	private WebsiteService websiteService;
	private AdService adService;
	private AccountService accountService;
	private EventDispatcher eventDispatcher;

	public ProposalService(ProposalRepository proposalRepository, WebsiteService websiteService, AdService adService,
			AccountService accountService, EventDispatcher eventDispatcher) {
		this.proposalRepository = proposalRepository;
		this.websiteService = websiteService;
		this.adService = adService;
		this.accountService = accountService;
		this.eventDispatcher = eventDispatcher;
	}

	public ServiceResponse<List<Proposal>> getProposalsByAccountId(String accountId, String embed) {
		List<Proposal> props = proposalRepository.getByAccountId(accountId);

		for (Proposal p : props) {
			if (embed.contains("website"))
				p.setWebsite(websiteService.getWebsiteById(p.getWebsiteId()).getReponse());

			p.setProposerName(accountService.getAccountNameById(p.getProposerId()));
			p.setProposeeName(accountService.getAccountNameById(p.getProposeeId()));
		}

		return ServiceResponse.ok(props);
	}

	public ServiceResponse<Proposal> getProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (!proposal.isAuthorized(accountId))
			return ServiceResponse.unauthorized();

		return ServiceResponse.ok(proposal);
	}

	public ServiceResponse<Proposal> createProposal(String accountId, String websiteId, String adId, String duration,
			String paymentMethod, String paymentValue) {

		ServiceResponseType validation = validateProposalFields(websiteId, adId, duration, paymentMethod, paymentValue);
		if (validation != ServiceResponseType.OK)
			return ServiceResponse.error(validation);

		Proposal proposal = new Proposal();

		proposal.setProposerId(accountId);
		proposal.setProposeeId(websiteService.getAccountIdUsingWebsiteId(websiteId));

		proposal.setWebsiteId(websiteId);
		proposal.setAdId(adId);
		proposal.setDuration(Integer.parseInt(duration));
		proposal.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		proposal.setPaymentValue(paymentValue);

		Proposal saved = proposalRepository.save(proposal);

		eventDispatcher.dispatch(new ProposalCreatedEvent(saved.clone()));

		return ServiceResponse.ok(saved);
	}

	public ServiceResponse<Nothing> deleteProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (proposal.isRejected()) {
			if (!containsProposalInNew(accountId, proposal))
				return ServiceResponse.proposalNotInNew();
		} else {
			if (!containsProposalInSent(accountId, proposal))
				return ServiceResponse.proposalNotInSent();
		}

		proposalRepository.deleteById(id);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> rejectProposalById(String accountId, String id) {
		Proposal proposal = proposalRepository.getById(id);

		if (!containsProposalInNew(accountId, proposal)) // Only proposals in new can be rejected
			return ServiceResponse.proposalNotInNew();

		proposal.setRejected(true);
		swapProposalLocation(proposal);

		eventDispatcher.dispatch(new ProposalRejectedEvent(proposal.clone(), accountId));

		if (proposal.getProposerId().equals(accountId)) {
			proposal.setProposerId("");
		} else {
			proposal.setProposeeId("");
		}

		proposalRepository.save(proposal);

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> reviewProposal(String accountId, String id, String duration, String paymentMethod,
			String paymentValue) {
		if (!validateDuration(duration))
			return ServiceResponse.error(ServiceResponseType.INVALID_DURATION);
		if (!validatePaymentMethod(paymentMethod))
			return ServiceResponse.error(ServiceResponseType.INVALID_PAYMENT_METHOD);
		if (!AEUtils.validateMonetaryValue(paymentValue))
			return ServiceResponse.error(ServiceResponseType.INVALID_PAYMENT_VALUE);

		Proposal prop = proposalRepository.getById(id);

		if (prop == null)
			return ServiceResponse.fail("Can't find a Proposal using the given id");

		if (!containsProposalInNew(accountId, prop))
			return ServiceResponse.proposalNotInNew(); // User can only reject proposals in new

		prop.setDuration(Integer.parseInt(duration));
		prop.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
		prop.setPaymentValue(paymentValue);
		prop.setVersion(prop.getVersion() + 1);
		swapProposalLocation(prop);

		proposalRepository.save(prop);

		eventDispatcher.dispatch(new ProposalReviewedEvent(prop.clone(), accountId));

		return ServiceResponse.ok(null);
	}

	public ServiceResponse<Nothing> acceptProposal(String accountId, String id) {
		Proposal prop = proposalRepository.getById(id);

		if (!containsProposalInNew(accountId, prop))
			return ServiceResponse.proposalNotInNew();

		if (!prop.getProposeeId().equals(accountId))
			return ServiceResponse.unauthorized();

		eventDispatcher.dispatch(new ProposalAcceptedEvent(prop.clone()));

		proposalRepository.deleteById(id);

		return ServiceResponse.ok(null);
	}

	/**
	 * Swaps the proposal location for the proposer and proposee. If the proposal is
	 * in 'new' for the proposer it will go to 'sent', if it's on 'sent' it will go
	 * to 'new' and the same thing for the proposee.
	 * 
	 * @param proposal
	 */
	private void swapProposalLocation(Proposal proposal) {
		proposal.setInProposerSent(!proposal.isInProposerSent());
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

		if (!validateDuration(duration))
			return ServiceResponseType.INVALID_DURATION;

		if (!validatePaymentMethod(paymentMethod))
			return ServiceResponseType.INVALID_PAYMENT_METHOD;

		if (!AEUtils.validateMonetaryValue(paymentValue))
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
		return adId != null && adService.getAdById(adId, "").getReponse() != null;
	}

	private boolean containsProposalInSent(String accountId, Proposal proposal) {
		return proposal.isInProposerSent() && proposal.getProposerId().equals(accountId)
				|| !proposal.isInProposerSent() && proposal.getProposeeId().equals(accountId);
	}

	private boolean containsProposalInNew(String accountId, Proposal proposal) {
		return !proposal.isInProposerSent() && proposal.getProposerId().equals(accountId)
				|| proposal.isInProposerSent() && proposal.getProposeeId().equals(accountId);
	}

}
