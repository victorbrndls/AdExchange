package com.harystolho.adexchange.log;

import com.harystolho.adexchange.log.Log.Identifier;

public interface Logger {

	String getName();

	void info(String msg);

	void info(String msg, String arg1, String arg2, String arg3);

	void info(Identifier identifier, String msg, String arg1, String arg2, String arg3);

	void error(String msg);

	void error(String msg, String arg1);

	void error(String msg, String arg1, String arg2, String arg3);

}
