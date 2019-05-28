package com.harystolho.adexchange.repositories.userdata;

import java.util.List;

import com.harystolho.adexchange.notifications.Notification;

public interface UserDataRepository {

	/**
	 * @param userId
	 * @return the notifications or an empty array
	 */
	List<Notification> getNotifications(String userId);

	void saveNotifications(String userId, List<Notification> notifications);

}
