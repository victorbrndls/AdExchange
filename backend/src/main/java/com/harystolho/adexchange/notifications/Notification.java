package com.harystolho.adexchange.notifications;

public class Notification {

	public enum NotificationType {
		NEW_PROPOSAL, ACCEPTED_PROPOSAL, RESENT_PROPOSAL, REJECTED_PROPOSAL
	}

	protected NotificationType type;

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

}
