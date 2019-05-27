package com.harystolho.adserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.harystolho.adserver.services.CacheService;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceTest {

	@InjectMocks
	CacheService<String> cacheService;

	@Before
	public void beforTest() {
		cacheService.clear();
	}

	@Test
	public void getNonPresentValue() {
		assertEquals(null, cacheService.get("fawfawfawfawf"));
	}

	@Test
	public void getValueWithNullKey() {
		assertEquals(null, cacheService.get(null));
	}

	@Test
	public void getPresentValue() {
		cacheService.store("123", "wa");

		assertEquals("wa", cacheService.get("123"));
	}

	@Test
	public void storeAndGetNullValue() {
		cacheService.store(null, "123");

		assertNotEquals("123", cacheService.get(null));
	}

	@Test
	public void removeKey() {
		cacheService.store("1", "b");

		assertEquals("b", cacheService.get("1"));

		cacheService.clear();

		assertEquals(null, cacheService.get("1"));
	}

	@Test
	public void shouldNotRemoveEntryIfNotExpired() {
		cacheService.store("12", "hi", Duration.ofHours(1));

		cacheService.cleanUp();

		assertEquals("hi", cacheService.get("12"));
	}

	@Test
	public void shouldRemoveEntryIfExpired() {
		cacheService.store("12", "hi", Duration.ofMillis(1));

		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cacheService.cleanUp();

		assertEquals(null, cacheService.get("12"));
	}

	@Test
	public void shouldNotAddEntryWithNegativeDuration() {
		cacheService.store("abc", "world", Duration.ofDays(-2));

		assertEquals(null, cacheService.get("abc"));
	}

}
