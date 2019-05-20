package com.harystolho.adexchange.models.account;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents monetary value for an {@link Account}.
 * 
 * <pre>
 * Rounding Rules: 
 * - The balance value MUST have at most 2 decimal points
 * - The balance value MUST be bigger or equal to 0
 * </pre>
 * 
 * @author Harystolho
 *
 */
public class Balance {

	// Use setBalance() to set this value
	private BigDecimal value;

	public Balance() {
		this("0.00");
	}

	public Balance(String value) throws BalanceException, NumberFormatException {
		setBalance(new BigDecimal(value));
	}

	public Balance(BigDecimal value) throws BalanceException {
		setBalance(value);
	}

	private void setBalance(BigDecimal value) throws BalanceException {
		if (value.compareTo(BigDecimal.ZERO) == -1)
			throw new BalanceException("Number can't be smaller than 0");

		this.value = roundValue(value);
	}

	/**
	 * Rounds the number according to the {@link Balance} rounding rule(Read the
	 * documentation for {@link Balance this} class to understand the rule).
	 * 
	 * @param value
	 * @return
	 */
	private BigDecimal roundValue(BigDecimal value) {
		int numbersBeforePoint = String.valueOf(value.intValue()).length();
		int precision = numbersBeforePoint + 2; // Numbers before the point + 2 because it must round to at most 2
												// decimal points

		if (value.compareTo(BigDecimal.ONE) == -1) {
			/*
			 * For some reason the rounding for numbers < 1 doesn't work. The workaround is
			 * to add 1 to that number, round the new number and then remove 1 from it you
			 * get the correct result.
			 */
			return value.add(BigDecimal.ONE).round(new MathContext(precision)).subtract(BigDecimal.ONE);
		} else {
			return value.round(new MathContext(precision));
		}
	}

	/**
	 * Adds the {@code other} balance to {@code this} and returns a new
	 * {@link Balance}
	 * 
	 * @param other
	 * @return
	 */
	public Balance add(Balance other) {
		return new Balance(this.value.add(other.value));
	}

	@Override
	public String toString() {
		return value.setScale(2).toPlainString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Balance) {
			Balance b = (Balance) obj;

			if (this.value.compareTo(b.value) == 0)
				return true;
		}

		return false;
	}

	public static class BalanceException extends RuntimeException {

		public BalanceException(String message) {
			super(message);
		}
	}

}
