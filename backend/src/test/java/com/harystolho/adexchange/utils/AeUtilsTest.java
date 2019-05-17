package com.harystolho.adexchange.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AeUtilsTest {

	@Test
	public void invalidMoneyValuesShouldFail() {
		List<String> list = Arrays.asList("1.", "2.5", ".0", ".", ",", ",,.", "0.-", "-=", "abs", "zero,onetwo", "1,",
				"2,478", ",874", "  ,", "0,00", "1.01", "00,00", "");

		for (String value : list) {
			assertEquals("Value: " + value, false, AEUtils.validateMonetaryValue(value));
		}
	}

	@Test
	public void validMoneyValuesShouldWork() {
		List<String> list = Arrays.asList("1,0", "2,5", "1,10", "0,10", "0,99", "157,00", "14", "99", "0,99", "14,1",
				"1,99", "0,01");

		for (String value : list) {
			assertEquals("Value: " + value, true, AEUtils.validateMonetaryValue(value));
		}
	}

}
