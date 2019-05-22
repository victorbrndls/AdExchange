package com.harystolho.adexchange.log;

import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
public class Log {

	public enum Level {
		INFO, ERROR
	}

	private final LocalTime createdAt;
	private final Level level;

	// The all fiels can be null
	// The null ones aren't saved to the database

	private String message;

	// Used by mongo
	@SuppressWarnings("unused")
	private Log(LocalTime createdAt, Level level, String message) {
		this.createdAt = createdAt;
		this.level = level;
		this.message = message;
	}

	private Log(Level level) {
		this.createdAt = LocalTime.now();
		this.level = level;
	}

	public Log(Level level, String message) {
		this(level);

		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalTime getCreatedAt() {
		return createdAt;
	}

	public Level getLevel() {
		return level;
	}

}
