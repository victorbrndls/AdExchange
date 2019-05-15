package com.harystolho.adexchange.converters;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.notifications.ProposalNotification;
import com.harystolho.adexchange.notifications.Notification.NotificationType;

@ReadingConverter
public class NotificationReaderConverter implements Converter<Document, Notification> {

	@Override
	public Notification convert(Document source) {
		NotificationType type = NotificationType.valueOf(source.getString("type"));

		switch (type) {
		case ACCEPTED_PROPOSAL:
			return new ProposalNotification.Accepted(source.getString("websiteName"));
		case REJECTED_PROPOSAL:
			return new ProposalNotification.Rejected(source.getString("senderName"), source.getString("websiteName"));
		case RESENT_PROPOSAL:
			return new ProposalNotification.Resent(source.getString("senderName"), source.getString("websiteName"));
		case NEW_PROPOSAL:
			return new ProposalNotification.New(source.getString("senderName"), source.getString("websiteName"));
		default:
			return null;
		}
	}

}