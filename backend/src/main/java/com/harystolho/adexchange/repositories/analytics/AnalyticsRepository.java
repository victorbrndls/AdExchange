package com.harystolho.adexchange.repositories.analytics;

public interface AnalyticsRepository {

	public void incrementTotalClicks(String modelId);

	public void incrementUniqueClicks(String modelId);

	public void incrementTotalViews(String modelId);

	public void incrementUniqueViews(String modelId);
	
}
