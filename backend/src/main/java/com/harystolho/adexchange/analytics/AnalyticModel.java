package com.harystolho.adexchange.analytics;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("analytics")
public class AnalyticModel {

	private String id;

	private String modelId;
	private LocalDate date;

	private int totalClicks;
	private int uniqueClicks;

	private int totalViews;
	private int uniqueViews;

	public AnalyticModel() {
		this.totalClicks = 0;
		this.uniqueClicks = 0;
		this.totalViews = 0;
		this.uniqueViews = 0;

		this.date = LocalDate.now();
	}

	public AnalyticModel(String id, String modelId, LocalDate date, int totalClicks, int uniqueClicks, int totalViews,
			int uniqueViews) {
		this.id = id;
		this.modelId = modelId;
		this.date = date;
		this.totalClicks = totalClicks;
		this.uniqueClicks = uniqueClicks;
		this.totalViews = totalViews;
		this.uniqueViews = uniqueViews;
	}

	public String getId() {
		return id;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getTotalClicks() {
		return totalClicks;
	}

	public int getUniqueClicks() {
		return uniqueClicks;
	}

	public int getTotalViews() {
		return totalViews;
	}

	public int getUniqueViews() {
		return uniqueViews;
	}

}
