package com.harystolho.adexchange.repositories.analytics;

import java.util.List;

import com.harystolho.adexchange.analytics.AnalyticModel;

public interface AnalyticsRepository {

	public List<AnalyticModel> get(String modelId);
	
	public void incrementTotalClicks(String modelId);

	public void incrementUniqueClicks(String modelId);

	public void incrementTotalViews(String modelId);

	public void incrementUniqueViews(String modelId);
	
}
