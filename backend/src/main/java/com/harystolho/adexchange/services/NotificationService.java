package com.harystolho.adexchange.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.notifications.Notification;

@Service
public class NotificationService {

	public ServiceResponse<List<Notification>> getNotificationsForUser(String accountId) {
		return ServiceResponse.ok(null);
	}

}
