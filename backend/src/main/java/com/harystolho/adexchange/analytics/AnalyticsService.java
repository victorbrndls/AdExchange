package com.harystolho.adexchange.analytics;

import java.util.List;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.repositories.analytics.AnalyticsRepository;
import com.harystolho.adexchange.services.ServiceResponse;

@Service
public class AnalyticsService {

	private AnalyticsRepository analyticsRepository;

	public AnalyticsService(AnalyticsRepository analyticsRepository) {
		this.analyticsRepository = analyticsRepository;
	}

	public ServiceResponse<List<AnalyticModel>> getAnalyticsForModel(String accountId, String modelId) {
		return ServiceResponse.ok(analyticsRepository.get(modelId));
	}

	public void incrementTotalClicks(String modelId) {
		analyticsRepository.incrementTotalClicks(modelId);
	}

	public void incrementUniqueClicks(String modelId) {
		incrementTotalClicks(modelId);

		analyticsRepository.incrementUniqueClicks(modelId);
	}

	public void incrementTotalViews(String modelId) {
		analyticsRepository.incrementTotalViews(modelId);
	}

	public void incrementUniqueViews(String modelId) {
		incrementTotalViews(modelId);

		analyticsRepository.incrementUniqueViews(modelId);
	}

}
