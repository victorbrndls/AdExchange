package com.harystolho.adexchange.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.util.StringUtils;

import com.harystolho.adserver.tracker.UserTrackerService;

public class AEUtils {

	public static final String ADMIN_ACCESS_ID = "ADMIN";

	public static final String corsOrigin = "http://localhost:8081";

	public static final String URL_REGEX = "^(https{0,1}:\\/\\/)\\S+";

	public static boolean isIdValid(String id) {
		return StringUtils.hasText(id);
	}

	/**
	 * Validates the money value that is used in this application. The number must
	 * have at most 2 decimal cases and use a comma(,) as the separator. Numbers
	 * bigger than 1000 mustn't include the dot(.)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean validateMonetaryValue(String value) {
		if (isValueValidForAccountBalance(value)) {
			value = value.replace(',', '.');
			double dValue = Double.parseDouble(value);

			if (dValue > 0.00)
				return true;
		}

		return false;
	}

	/**
	 * A monetary value has to be bigger than 0.00 but the account balance can be
	 * 0.00
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isValueValidForAccountBalance(String value) {
		try {
			int occurences = StringUtils.countOccurrencesOf(value, ",");

			if (occurences > 1) // Can't contain more than one ','
				return false;

			if (value.contains(","))
				if (value.split("\\,")[1].length() > 2) // If there are more than 2 cases after the ','
					return false;

			if (value.contains("."))
				return false;

			value = value.replace(',', '.');
			Double.parseDouble(value); // Try to cast to double to make sure it's a number
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static Cookie getCookieByName(Cookie[] cookies, String cookieName) {
		if (cookies == null)
			return null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(UserTrackerService.COOKIE_NAME))
				return cookie;
		}

		return null;
	}

	public static String generateUUIDString(int strength) {
		String finalID = "";

		for (int x = 0; x < strength; x++) {
			finalID += UUID.randomUUID().toString().replaceAll("-", "");
		}

		return finalID;
	}

	public static boolean verifyEmail(String email) {
		return email != null ? email.matches("([\\w.]+@[\\w.]+)") : false;
	}
}
