package com.harystolho.adexchange.models.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;

import com.harystolho.adexchange.models.account.Balance.BalanceException;
import com.harystolho.adexchange.utils.Pair;

public class BalanceTest {

	@Test
	public void createBalanceWithValidValues() {
		for (String value : Arrays.array("0", "0.0", "1", "99", "1.11", "0.99", "19457", "999999", "0.0000009",
				"12.212121", "1.111111")) {
			try {
				new Balance(value);
			} catch (Exception e) {
				fail();
			}
		}
	}

	@Test(expected = Exception.class)
	public void createBalanceWithInvalidValues() throws BalanceException, NumberFormatException {
		for (String value : Arrays.array("", "-1", "-0", "-0.77777", "0.0.0.0", ".", ",", " ", "awdaw", "-=-12t32")) {
			new Balance(value);
		}
	}

	@Test
	public void roundValuesShouldWork() {
		List<Pair<String, String>> values = new ArrayList<>();

		values.add(Pair.of("0.000000000000", "0.00"));
		values.add(Pair.of("0.0009", "0.00"));
		values.add(Pair.of("0.009", "0.01"));
		values.add(Pair.of("0.50", "0.50"));
		values.add(Pair.of("0.99", "0.99"));
		values.add(Pair.of("1.0", "1.0"));
		values.add(Pair.of("1.0899", "1.09"));
		values.add(Pair.of("1.1111119", "1.11"));
		values.add(Pair.of("1.99", "1.99"));
		values.add(Pair.of("2.999", "3.00"));
		values.add(Pair.of("10", "10"));
		values.add(Pair.of("10.458", "10.46"));
		values.add(Pair.of("10.99", "10.99"));
		values.add(Pair.of("15.999", "16.00"));
		values.add(Pair.of("99.99", "99.99"));
		values.add(Pair.of("99.999", "100.0"));
		values.add(Pair.of("400.996", "401.00"));

		for (Pair<String, String> pair : values) {

			Balance balance = new Balance(pair.getFist());
			assertEquals(pair.getSecond(), balance.toString());
		}
	}

}
