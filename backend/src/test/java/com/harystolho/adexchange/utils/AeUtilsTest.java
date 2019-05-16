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

}
