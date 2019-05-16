package com.harystolho.adexchange.utils;

import org.springframework.util.StringUtils;

public class AEUtils {

	public static final String ADMIN_ACESS_ID = "ADMIN";

	public static final String corsOrigin = "http://localhost:8081";

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
		try {
			int occurences = StringUtils.countOccurrencesOf(value, ",");

			if (occurences > 1) // Can't contain more than one ','
				return false;
			
			if (value.contains(","))
				if (value.split("\\.")[1].length() > 2) // If there are more than 2 cases after the ','
					return false;

			if(value.contains("."))
				return false;
			
			double dValue = Double.parseDouble(value); // Try to cast to double to make sure it's a number

			if (dValue <= 0.0)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
