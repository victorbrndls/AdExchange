package com.harystolho.adServer;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class AdModelCache {

	private ConcurrentMap<String, CacheObject<AdModel>> cache;

	public AdModelCache() {
		cache = new ConcurrentHashMap<>();
	}

	/**
	 * @param id
	 * @return returns the {@link AdModel} present in the cache or <code>null</code>
	 */
	public AdModel get(String id) {
		return null;
	}

	public void store(String key, AdModel ad) {
		store(key, ad, Duration.ofHours(1));
	}

	public void store(String key, AdModel ad, Duration duration) {
		if (key == null && ad == null)
			return;

		cache.put(key, new CacheObject<AdModel>(ad, System.currentTimeMillis() + duration.toMillis()));
	}

	public void evict(Object key) {

	}

	public void clear() {
		cache.clear();
	}

	/**
	 * Removes entries that have expired
	 */
	public void cleanUp() {
		cache.entrySet().removeIf((entry) -> entry.getValue().hasExpired());
	}

	public static class CacheObject<T> {

		private T object;
		private long expiration;

		public CacheObject(T object, long expiration) {
			this.object = object;
			this.expiration = expiration;
		}

		public T getObject() {
			return object;
		}

		public long getExpiration() {
			return expiration;
		}

		public boolean hasExpired() {
			return System.currentTimeMillis() > expiration;
		}

	}
}
