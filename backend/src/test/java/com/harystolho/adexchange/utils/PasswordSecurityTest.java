package com.harystolho.adexchange.utils;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class PasswordSecurityTest {

	@Test
	public void encryptedPasswordShouldBeDifferentFromPassword() {
		String encryptPassword = PasswordSecurity.encryptPassword("some_pass123");

		assertNotEquals("some_pass123", encryptPassword);
	}

}
