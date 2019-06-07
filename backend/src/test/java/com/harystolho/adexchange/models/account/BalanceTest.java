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
		values.add(Pair.of("0.0059", "0.01"));
		values.add(Pair.of("0.0456", "0.05"));
		values.add(Pair.of("0.456", "0.46"));
		values.add(Pair.of("0.756", "0.76"));
		values.add(Pair.of("0.50", "0.50"));
		values.add(Pair.of("0.99", "0.99"));
		values.add(Pair.of("1.0", "1.00"));
		values.add(Pair.of("1.0899", "1.09"));
		values.add(Pair.of("1.1111119", "1.11"));
		values.add(Pair.of("1.99", "1.99"));
		values.add(Pair.of("2.999", "3.00"));
		values.add(Pair.of("2.267", "2.27"));
		values.add(Pair.of("10", "10.00"));
		values.add(Pair.of("10.458", "10.46"));
		values.add(Pair.of("10.99", "10.99"));
		values.add(Pair.of("15.999", "16.00"));
		values.add(Pair.of("99.99", "99.99"));
		values.add(Pair.of("99.999", "100.00"));
		values.add(Pair.of("400.996", "401.00"));

		for (Pair<String, String> pair : values) {
			Balance balance = new Balance(pair.getFirst());
			assertEquals(pair.getSecond(), balance.toString());
		}
	}

	@Test
	public void addBalancesShouldWork() {
		assertEquals("0.00", new Balance("0.0").add(new Balance("0.0")).toString());
		assertEquals("0.90", new Balance("0.0").add(new Balance("0.9")).toString());
		assertEquals("23.20", new Balance("0.0").add(new Balance("23.20")).toString());
		assertEquals("1.97", new Balance("1.96").add(new Balance("0.01")).toString());
		assertEquals("4.40", new Balance("1.50").add(new Balance("2.9")).toString());
		assertEquals("10.99", new Balance("9.49").add(new Balance("1.50")).toString());
		assertEquals("29.99", new Balance("23.23").add(new Balance("6.76")).toString());
		assertEquals("99.99", new Balance("0.0").add(new Balance("99.99")).toString());
		assertEquals("100.11", new Balance("99.01").add(new Balance("1.10")).toString());
	}

	@Test
	public void compareBalances() {
		Balance b1 = new Balance("7.10");
		Balance b2 = new Balance("4.70");
		Balance b3 = new Balance("7.10");

		assertEquals(1, b1.compare(b2));
		assertEquals(-1, b2.compare(b1));
		
		assertEquals(0, b1.compare(b3));
	}

}
