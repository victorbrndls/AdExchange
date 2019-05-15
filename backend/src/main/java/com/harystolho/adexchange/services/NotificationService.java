package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class NotificationService {

	private WebsiteService websiteService;
	private UserDataService userDataService;

	private NotificationService(WebsiteService websiteService, UserDataService userDataService) {
		this.websiteService = websiteService;
		this.userDataService = userDataService;
	}

	public ServiceResponse<List<Notification>> getNotificationsForUser(String accountId) {
		return ServiceResponse.ok(userDataService.getNotificationsForUser(accountId));
	}

	public ServiceResponseType emitNewProposalNotification(Proposal proposal) {
		String senderName = "Victor"; // proposal.getProposerId();
		Website website = websiteService.getWebsiteById(proposal.getWebsiteId()).getReponse();

		if (website == null)
			return ServiceResponseType.INVALID_WEBSITE_ID;

		Notification notif = new Notification.NewProposal(senderName, website.getName());

		userDataService.addNotificationToUser(notif, proposal.getProposeeId());

		return ServiceResponseType.OK;
	}

}
