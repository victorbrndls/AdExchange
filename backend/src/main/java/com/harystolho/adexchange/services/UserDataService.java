package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.repositories.userdata.UserDataRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class UserDataService {

	protected static final int MAX_NOTIFICATIONS = 15;

	private UserDataRepository userDataRepository;

	private UserDataService(UserDataRepository userDataRepository) {
		this.userDataRepository = userDataRepository;
	}

	public ServiceResponseType addNotificationToUser(Notification notif, String userId) {
		List<Notification> notifications = userDataRepository.getNotifications(userId);

		if (notifications.size() >= MAX_NOTIFICATIONS)
			removeExcessNotifications(notifications);

		notifications.add(notif);

		userDataRepository.saveNotifications(userId, notifications);

		return ServiceResponseType.OK;
	}

	public List<Notification> getNotificationsForUser(String userId) {
		return userDataRepository.getNotifications(userId);
	}

	private void removeExcessNotifications(List<Notification> notifications) {
		int notificationsToRemove = notifications.size() - MAX_NOTIFICATIONS + 1;

		for (int i = 0; i <= notificationsToRemove; i++) {
			notifications.remove(0);
		}
	}
}
