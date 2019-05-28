package com.harystolho.adexchange.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.notifications.ProposalNotification;
import com.harystolho.adexchange.repositories.userdata.UserDataRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserDataServiceTest {

	@InjectMocks
	UserDataService userDataService;

	@Mock
	UserDataRepository userDataRepository;

	@Test
	public void removeExcessNotifications_ShouldWork() {
		List<Notification> notifs = new ArrayList<>();

		for (int i = 0; i < UserDataService.MAX_NOTIFICATIONS + 5; i++) {
			notifs.add(new ProposalNotification.Accepted(""));
		}

		Mockito.when(userDataRepository.getNotifications("aa1")).thenReturn(notifs);

		userDataService.addNotificationToUser(new ProposalNotification.Accepted(""), "aa1");

		Mockito.verify(userDataRepository).saveNotifications(Mockito.anyString(), Mockito.argThat((arg) -> {
			return arg.size() < UserDataService.MAX_NOTIFICATIONS;
		}));
	}

}
