package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.repositories.userdata.UserDataRepository;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;

@Service
public class UserDataService {

	private UserDataRepository userDataRepository;

	private UserDataService(UserDataRepository userDataRepository) {
		this.userDataRepository = userDataRepository;
	}

	public ServiceResponseType addNotificationToUser(Notification notif, String userId) {
		List<Notification> notifications = userDataRepository.getNotifications(userId);

		notifications.add(notif);

		userDataRepository.saveNotifications(userId, notifications);

		return ServiceResponseType.OK;
	}

	public List<Notification> getNotificationsForUser(String userId) {
		return userDataRepository.getNotifications(userId);
	}

}
