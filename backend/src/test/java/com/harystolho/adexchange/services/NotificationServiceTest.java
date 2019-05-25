package com.harystolho.adexchange.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.models.Proposal;
import com.harystolho.adexchange.models.Website;
import com.harystolho.adexchange.models.account.Account;
import com.harystolho.adexchange.notifications.Notification.NotificationType;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

	@InjectMocks
	NotificationService notificationService;

	@Mock
	WebsiteService websiteService;
	@Mock
	UserDataService userDataService;
	@Mock
	AccountService accountService;

	@Test
	public void newProposalNotification_ShouldWork() {
		Proposal prop = new Proposal();
		prop.setProposerId("npn1_1");
		prop.setProposeeId("npn1_2");
		prop.setWebsiteId("npnw1");

		Website w = new Website(null, null);
		Account a = new Account();

		Mockito.when(accountService.getAccountById("npn1_1")).thenReturn(ServiceResponse.ok(a));
		Mockito.when(websiteService.getWebsiteById("npnw1")).thenReturn(ServiceResponse.ok(w));

		notificationService.emitNewProposalNotification(prop);

		Mockito.verify(userDataService).addNotificationToUser(
				Mockito.argThat(am -> am.getType() == NotificationType.NEW_PROPOSAL), Mockito.same("npn1_2"));
	}

	@Test
	public void rejectedProposalNotification_ShouldWork() {
		Proposal prop = new Proposal();
		prop.setProposerId("rpn1");
		prop.setProposeeId("rpn2");
		prop.setWebsiteId("rpnw1");

		Website w = new Website(null, null);
		Account a = new Account();

		Mockito.when(accountService.getAccountById("rpn2")).thenReturn(ServiceResponse.ok(a));
		Mockito.when(websiteService.getWebsiteById("rpnw1")).thenReturn(ServiceResponse.ok(w));

		notificationService.emitRejectedProposalNotification(prop, "rpn2");

		Mockito.verify(userDataService).addNotificationToUser(
				Mockito.argThat(am -> am.getType() == NotificationType.REJECTED_PROPOSAL), Mockito.same("rpn1"));
	}

	@Test
	public void reviewedProposalNotification_ShouldWork() {
		Proposal prop = new Proposal();
		prop.setProposerId("ac1");
		prop.setProposeeId("ac2");
		prop.setWebsiteId("wc1");

		Website w = new Website(null, null);
		Account a = new Account();

		Mockito.when(accountService.getAccountById("ac1")).thenReturn(ServiceResponse.ok(a));
		Mockito.when(websiteService.getWebsiteById("wc1")).thenReturn(ServiceResponse.ok(w));

		notificationService.emitReviewedProposalNotification(prop, "ac1");

		Mockito.verify(userDataService).addNotificationToUser(
				Mockito.argThat(am -> am.getType() == NotificationType.REVIEWED_PROPOSAL), Mockito.same("ac2"));
	}
	
	@Test
	public void acceptedProposalNotification_ShouldWork() {
		Proposal prop = new Proposal();
		prop.setProposerId("ad1");
		prop.setProposeeId("ad2");
		prop.setWebsiteId("wd1");

		Website w = new Website(null, null);

		Mockito.when(websiteService.getWebsiteById("wd1")).thenReturn(ServiceResponse.ok(w));

		notificationService.emitAcceptedProposalNotification(prop);

		Mockito.verify(userDataService).addNotificationToUser(
				Mockito.argThat(am -> am.getType() == NotificationType.ACCEPTED_PROPOSAL), Mockito.same("ad1"));
	}

}
