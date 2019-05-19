package com.harystolho.adexchange.models.account;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents monetary value for an {@link Account}.
 * 
 * <pre>
 * Rounding Rules: 
 * - The balance value MUST have at most 2 decimal points
 * - The balance value MUST be bigger or equal than 0
 * </pre>
 * 
 * @author Harystolho
 *
 */
public class Balance {

	private static final BigDecimal ZERO_POINT_ONE = new BigDecimal("0.1");
	private static final BigDecimal MINUS_ZERO_POINT_ONE = new BigDecimal("-0.1");

	// Use setBalance() to set this value
	private BigDecimal value;

	public Balance(String value) throws BalanceException, NumberFormatException {
		setBalance(new BigDecimal(value));
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

		if (value.compareTo(ZERO_POINT_ONE) == -1 && value.compareTo(MINUS_ZERO_POINT_ONE) == 1) {
			/*
			 * For some reason the rounding for numbers > -0.1 && < 0.1 doesn't work. The
			 * workaround is to add 1 to that number, round the new number and then remove 1
			 * from it you get the correct result.
			 */
			return value.add(BigDecimal.ONE).round(new MathContext(precision)).subtract(BigDecimal.ONE);
		} else {
			return value.round(new MathContext(precision));
		}
	}

	@Override
	public String toString() {
		return value.toPlainString();
	}

	public static class BalanceException extends RuntimeException {

		public BalanceException(String message) {
			super(message);
		}
	}

}
