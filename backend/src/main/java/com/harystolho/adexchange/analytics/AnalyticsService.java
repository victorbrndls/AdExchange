package com.harystolho.adexchange.analytics;

import org.springframework.stereotype.Service;

import com.harystolho.adexchange.repositories.analytics.AnalyticsRepository;

@Service
public class AnalyticsService {

	private AnalyticsRepository analyticsRepository;

	public AnalyticsService(AnalyticsRepository analyticsRepository) {
		this.analyticsRepository = analyticsRepository;
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
