package com.harystolho.adexchange.notifications;

public class Notification {

	public enum NotificationType {
		NEW_PROPOSAL, ACCEPTED_PROPOSAL, RESENT_PROPOSAL, REJECTED_PROPOSAL
	}

	protected String senderName;
	protected String websiteName;
	protected NotificationType type;

	public Notification() {
	}

	private Notification(NotificationType type) {
		this.type = type;
	}

	private Notification(NotificationType type, String senderName, String websiteName) {
		this(type);
		this.senderName = senderName;
		this.websiteName = websiteName;
	}

	public static class NewProposal extends Notification {

		public NewProposal(String senderName, String websiteName) {
			super(NotificationType.NEW_PROPOSAL, senderName, websiteName);
		}
	}

	public static class AcceptedProposal extends Notification {

		public AcceptedProposal(String websiteName) {
			super(NotificationType.ACCEPTED_PROPOSAL);
			this.websiteName = websiteName;
		}
	}

	public static class ResentProposal extends Notification {

		public ResentProposal(String senderName, String websiteName) {
			super(NotificationType.RESENT_PROPOSAL, senderName, websiteName);
		}
	}

	public static class RejectedProposal extends Notification {

		public RejectedProposal(String senderName, String websiteName) {
			super(NotificationType.REJECTED_PROPOSAL, senderName, websiteName);
		}
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

}
