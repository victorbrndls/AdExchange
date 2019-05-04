package com.harystolho.adServer.services;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.harystolho.adexchange.information.GlobalInformant;
import com.harystolho.adexchange.information.Visitor;

@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CacheService<T> implements Visitor {

	private static final Duration DEFAULT_DURATION = Duration.ofHours(1);

	private ConcurrentMap<String, CacheObject<T>> cache;

	public CacheService() {
		cache = new ConcurrentHashMap<>();
	}

	/**
	 * @param key
	 * @return returns the entry present in the cache or <code>null</code>
	 */
	public T get(String key) {
		if (key == null)
			return null;

		CacheObject<T> entry = cache.get(key);

		if (entry != null)
			return entry.getObject();

		return null;
	}

	public void store(String key, T value) {
		store(key, value, DEFAULT_DURATION);
	}

	public void store(String key, T value, Duration duration) {
		if (key == null || value == null || duration.isNegative())
			return;

		cache.put(key, new CacheObject<T>(value, System.currentTimeMillis() + duration.toMillis()));
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	public void evict(Object key) {
		cache.remove(key);
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

	@Override
	public ObjectNode visit(GlobalInformant informant) {
		ObjectNode node = informant.defaultObjectNode();
		node.put("id", this.getClass().getName());

		node.putPOJO("entries", cache);
		
		return node;
	}
}
