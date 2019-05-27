package com.harystolho.adexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = { "com.harystolho.adexchange", "com.harystolho.adserver" })
public class Main {

	static {
		System.setProperty("user.timezone", "UTC");
	}

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Main.class, args);
	}

}