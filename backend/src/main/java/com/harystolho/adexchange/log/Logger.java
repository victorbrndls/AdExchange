package com.harystolho.adexchange.log;

public interface Logger {

	String getName();

	void info(String msg);

	void info(String msg, String arg1, String arg2, String arg3);

	void error(String msg);

	void error(String msg, String arg1);

}
