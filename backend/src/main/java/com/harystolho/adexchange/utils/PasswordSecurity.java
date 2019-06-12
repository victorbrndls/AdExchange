package com.harystolho.adexchange.utils;

import org.springframework.util.Base64Utils;

/**
 * Encrypts and compares passwords
 * 
 * @author Harystolho
 *
 */
public class PasswordSecurity {

	public static String encryptPassword(String password) {
		return Base64Utils.encodeToString(password.getBytes());
	}

	/**
	 * @param password1
	 * @param password2
	 * @return TRUE if passwords match
	 */
	public static boolean comparePasswords(String password1, String password2) {
		return password1.equals(password2);
	}

}
