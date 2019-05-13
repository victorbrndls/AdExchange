package com.harystolho.adexchange.utils;

import org.springframework.util.StringUtils;

public class AEUtils {

	public static final String ADMIN_ACESS_ID = "ADMIN";
	
	public static final String corsOrigin = "http://localhost:8081";
	
	public static boolean isIdValid(String id) {
		return StringUtils.hasText(id);
	}
}
