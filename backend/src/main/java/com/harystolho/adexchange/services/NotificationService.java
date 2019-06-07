package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.notifications.ProposalNotification;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class NotificationService {

	private WebsiteService websiteService;
	private UserDataService userDataService;
	private AccountService accountService;

	private NotificationService(WebsiteService websiteService, UserDataService userDataService,
			AccountService accountService) {
		this.websiteService = websiteService;
		this.userDataService = userDataService;
		this.accountService = accountService;
	}

	public ServiceResponse<List<Notification>> getNotificationsForUser(String accountId) {
		return ServiceResponse.ok(userDataService.getNotificationsForUser(accountId));
	}

	public ServiceResponse<Boolean> getNotificationStatusForUser(String accountId) {
		return ServiceResponse.ok(userDataService.getNotificationsStatusForUser(accountId));
	}

	public ServiceResponseType setNotificationStatusForUser(String accountId, boolean status) {
		userDataService.setNotificationsStatusForUser(accountId, status);
		return ServiceResponseType.OK;
	}

	public ServiceResponseType emitNewProposalNotification(Proposal proposal) {
		Account account = accountService.getAccountById(proposal.getProposerId()).getReponse();
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (account == null)
			return ServiceResponseType.FAIL;
		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new ProposalNotification.New(account.getFullName(), website.getName());

		userDataService.addNotificationToUser(notif, proposal.getProposeeId());

		return ServiceResponseType.OK;
	}

	public ServiceResponseType emitRejectedProposalNotification(Proposal proposal, String rejectorId) {
		Account rejectorAccount = accountService.getAccountById(rejectorId).getReponse();
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (rejectorAccount == null)
			return ServiceResponseType.FAIL;
		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new ProposalNotification.Rejected(rejectorAccount.getFullName(), website.getName());

		userDataService.addNotificationToUser(notif, getOppositeId(proposal, rejectorId));

		return ServiceResponseType.OK;
	}

	public ServiceResponseType emitReviewedProposalNotification(Proposal proposal, String reviewerId) {
		Account reviewerAccount = accountService.getAccountById(reviewerId).getReponse();
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (reviewerAccount == null)
			return ServiceResponseType.FAIL;
		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new ProposalNotification.Reviewed(reviewerAccount.getFullName(), website.getName());

		userDataService.addNotificationToUser(notif, getOppositeId(proposal, reviewerId));

		return ServiceResponseType.OK;
	}

	public ServiceResponseType emitAcceptedProposalNotification(Proposal proposal) {
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new ProposalNotification.Accepted(website.getName());
		userDataService.addNotificationToUser(notif, proposal.getProposerId());

		return ServiceResponseType.OK;
	}

	/**
	 * If the {userId} is equal to the {@link Proposal#proposerId} than it returns
	 * the {@link Proposal#proposeeId}, otherwise it returns the
	 * {@link Proposal#proposerId}
	 * 
	 * @param proposal
	 * @param userId
	 * @return
	 */
	private String getOppositeId(Proposal proposal, String userId) {
		if (proposal.getProposerId().equals(userId)) {
			return proposal.getProposeeId();
		} else {
			return proposal.getProposerId();
		}
	}

}
