package com.harystolho.adexchange.log;

import java.time.LocalTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
public class Log {

	public enum Identifier {
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
	private Identifier identifier;

	// Used by mongo
	@SuppressWarnings("unused")
	private Log(LocalTime createdAt, Level level, String message, Identifier identifier) {
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

	public Log(Level level, Identifier id, String message) {
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

	public Identifier getIdentifier() {
		return identifier;
	}

}
