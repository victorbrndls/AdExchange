package com.harystolho.adexchange.log;

import java.util.IllegalFormatException;

import org.mockito.internal.util.StringUtil;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.harystolho.adexchange.log.Log.Level;

/**
 * Saves the logs to the database. This is used to store errors that need to be
 * reviewed by an admin, for example payment errors
 * 
 * @author Harystolho
 *
 */
@Service
public class MongoLoggerImpl implements Logger {

	private MongoOperations mongoOperations;

	public MongoLoggerImpl(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	@Override
	public String getName() {
		return "MongoLogger";
	}

	@Override
	public void info(String msg) {
		mongoOperations.save(new Log(Level.INFO, msg));
	}

	@Override
	public void info(String msg, String arg1, String arg2, String arg3) {
		mongoOperations.save(formatMessage(Level.INFO, msg, arg1, arg2, arg3));
	}

	@Override
	public void error(String msg) {
		mongoOperations.save(new Log(Level.ERROR, msg));
	}

	@Override
	public void error(String msg, String arg1) {
		mongoOperations.save(formatMessage(Level.ERROR, msg, arg1));
	}

	private Log formatMessage(Level level, String msg, Object... args) {
		try {
			return new Log(level, String.format(msg, args));
		} catch (IllegalFormatException e) {
			return new Log(level, msg);
		}
	}

}
