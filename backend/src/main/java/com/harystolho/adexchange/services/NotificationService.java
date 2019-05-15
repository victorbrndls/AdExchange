package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Account;
import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
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

	public ServiceResponseType emitNewProposalNotification(Proposal proposal) {
		Account account = accountService.getAccountById(proposal.getProposeeId()).getReponse();
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (account == null)
			return ServiceResponseType.FAIL;
		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new ProposalNotification.New(account.getFullName(), website.getName());

		userDataService.addNotificationToUser(notif, proposal.getProposeeId());

		return ServiceResponseType.OK;
	}

}
