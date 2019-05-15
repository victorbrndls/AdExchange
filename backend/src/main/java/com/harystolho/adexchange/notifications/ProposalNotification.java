package com.harystolho.adexchange.notifications;

public class ProposalNotification extends Notification {

	protected String senderName;
	protected String websiteName;

	public ProposalNotification() {
	}

	private ProposalNotification(NotificationType type) {
		this.type = type;
	}

	private ProposalNotification(NotificationType type, String senderName, String websiteName) {
		this(type);
		this.senderName = senderName;
		this.websiteName = websiteName;
	}

	public static class New extends ProposalNotification {

		public New(String senderName, String websiteName) {
			super(NotificationType.NEW_PROPOSAL, senderName, websiteName);
		}
	}

	public static class Accepted extends ProposalNotification {

		public Accepted(String websiteName) {
			this.type = NotificationType.ACCEPTED_PROPOSAL;
			this.websiteName = websiteName;
		}
	}

	public static class Reviewed extends ProposalNotification {

		public Reviewed(String senderName, String websiteName) {
			super(NotificationType.REVIEWED_PROPOSAL, senderName, websiteName);
		}
	}

	public static class Rejected extends ProposalNotification {

		public Rejected(String rejectorName, String websiteName) {
			super(NotificationType.REJECTED_PROPOSAL, rejectorName, websiteName);
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

}
