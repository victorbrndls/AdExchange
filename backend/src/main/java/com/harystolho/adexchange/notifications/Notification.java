package com.harystolho.adexchange.notifications;

public class Notification {

	public enum NotificationType {
		NEW_PROPOSAL, ACCEPTED_PROPOSAL, REVIEWED_PROPOSAL, REJECTED_PROPOSAL
	}

	protected NotificationType type;

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

}
