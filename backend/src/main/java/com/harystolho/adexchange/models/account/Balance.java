package com.harystolho.adexchange.models.account;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Represents monetary value for the {@link Account}
 * 
 * @author Harystolho
 *
 */
public class Balance {

	private static final MathContext mc = new MathContext(4); // Rounds number up with 2 decimal points

	// Use setBalance() to set this value
	private BigDecimal value;

	public Balance(String value) throws NumberFormatException {
		setBalance(new BigDecimal(value));
	}

	public void setBalance(BigDecimal value) {
		this.value = value.round(mc);
	}

	@Override
	public String toString() {
		return value.toPlainString();
	}

}
