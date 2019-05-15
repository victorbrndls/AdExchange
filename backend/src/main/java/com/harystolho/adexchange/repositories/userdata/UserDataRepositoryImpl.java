package com.harystolho.adexchange.repositories.userdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.models.UserData;
import com.harystolho.adexchange.notifications.Notification;

@Service
public class UserDataRepositoryImpl implements UserDataRepository {

	private MongoOperations mongoOperations;

	private UserDataRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public List<Notification> getNotifications(String userId) {
		Query query = Query.query(Criteria.where("_id").is(userId));
		query.fields().include("notifications");

		UserData userData = mongoOperations.findOne(query, UserData.class);

		return userData == null ? new ArrayList<>() : userData.getNotifications();
	}

	@Override
	public void saveNotifications(String userId, List<Notification> notifications) {
		Query query = Query.query(Criteria.where("_id").is(userId));

		if (mongoOperations.exists(query, UserData.class)) {
			Update update = Update.update("notifications", notifications);
			mongoOperations.updateFirst(query, update, UserData.class);
		} else {
			UserData ud = new UserData();
			ud.setId(userId);
			ud.setNotifications(notifications);
			mongoOperations.save(ud);
		}

	}

}
