package com.harystolho.adexchange.models.account;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.harystolho.adexchange.models.Contract;

public class ContractTest {

	@Test
	public void convertPaymentValueToDotNotation() {
		Contract c = new Contract();
		c.setPaymentValue("0,7");

		assertEquals("0.7", c.convertPaymentValueToDotNotation());
	}

}
