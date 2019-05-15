package com.harystolho.adexchange.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.harystolho.adexchange.notifications.Notification;

/**
 * The fields here belong to an {@link Account} but they are not in the same
 * document because I want to the email and password information to be in a
 * different document.The {@link UserData#id} is equal to the id of the account
 * it belongs to
 * 
 * @author Harystolho
 *
 */
@Document("userData")
public class UserData {

	private String id;
	private List<Notification> notifications;

	public UserData() {
		this.notifications = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public void addNotification(Notification notification) {
		this.notifications.add(notification);
	}

}
