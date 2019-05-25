package com.harystolho.adexchange.log;

import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
public class Log {

	public enum LogIdentifier {
		ACCOUNT_BALANCE_CHANGED //
	}

	public enum Level {
		INFO, ERROR
	}

	private final LocalTime createdAt;
	private final Level level;

	// All fields below can be null
	// The null ones aren't saved to the database

	private String message;
	private LogIdentifier identifier;

	// Used by mongo
	@SuppressWarnings("unused")
	private Log(LocalTime createdAt, Level level, String message, LogIdentifier identifier) {
		this.createdAt = createdAt;
		this.level = level;

		this.message = message;
		this.identifier = identifier;
	}

	private Log(Level level) {
		this.createdAt = LocalTime.now();
		this.level = level;
	}

	public Log(Level level, String message) {
		this(level);

		this.message = message;
	}

	public Log(Level level, LogIdentifier id, String message) {
		this(level, message);

		this.identifier = id;
	}

	public String getMessage() {
		return message;
	}

	public LocalTime getCreatedAt() {
		return createdAt;
	}

	public Level getLevel() {
		return level;
	}

	public LogIdentifier getIdentifier() {
		return identifier;
	}

}
