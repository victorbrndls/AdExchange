package com.harystolho.adexchange.repositories.analytics;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.analytics.AnalyticModel;

@Service
public class AnalyticsRepositoryImpl implements AnalyticsRepository {

	private MongoOperations mongoOperations;

	public AnalyticsRepositoryImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void incrementTotalClicks(String modelId) {
		findAndIncrementByOne(modelId, "totalClicks");
	}

	@Override
	public void incrementUniqueClicks(String modelId) {
		findAndIncrementByOne(modelId, "uniqueClicks");
	}

	@Override
	public void incrementTotalViews(String modelId) {
		findAndIncrementByOne(modelId, "totalViews");
	}

	@Override
	public void incrementUniqueViews(String modelId) {
		findAndIncrementByOne(modelId, "uniqueViews");
	}

	private void findAndIncrementByOne(String modelId, String field) {
		createIfDoesntExist(modelId);

		Query query = todaySelectionQuery(modelId);
		Update update = new Update().inc(field, 1);

		mongoOperations.findAndModify(query, update, AnalyticModel.class);
	}

	private void createIfDoesntExist(String modelId) {
		Query query = todaySelectionQuery(modelId);

		if (!mongoOperations.exists(query, AnalyticModel.class)) {
			AnalyticModel am = new AnalyticModel();
			am.setModelId(modelId);

			mongoOperations.save(am);
		}
	}

	private Query todaySelectionQuery(String modelId) {
		return Query.query(Criteria.where("modelId").is(modelId).and("date").is(LocalDate.now()));
	}

}
