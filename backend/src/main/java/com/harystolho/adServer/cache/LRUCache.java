package com.harystolho.adserver.cache;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LRUCache<T> {

	// Can't set this value using constructor because @Scheduled annotation doesn't
	// allow
	private final long CLEAN_UP_DELAY = 1000 * 60 * 2; // 2 Minutes
	private final long ENTRY_TTL;

	private Map<String, CacheObject<T>> cache;

	private EntriesEvictionListener<T> entriesEvictionListener;

	public LRUCache() {
		this(Duration.ofMinutes(5).toMillis());
	}

	public LRUCache(long entryTTL) {
		this.ENTRY_TTL = entryTTL;

		this.cache = new ConcurrentHashMap<>();
	}

	/**
	 * @param key
	 * @return returns the entry present in the cache or <code>null</code>
	 */
	public T get(String key) {
		CacheObject<T> entry = cache.get(key);

		if (entry != null) {
			entry.setLastAccessed(System.currentTimeMillis());
			return entry.getObject();
		}

		return null;
	}

	public void store(String key, T value) {
		if (key == null || value == null)
			throw new NullPointerException(String.format("%s can't be null", key == null ? "key" : "value"));

		cache.put(key, new CacheObject<T>(value));
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	public void evict(String key) {
		cache.remove(key);
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * Removes entries that have expired
	 */
	@Scheduled(fixedDelay = CLEAN_UP_DELAY)
	public void cleanUp() {
		Iterator<Entry<String, CacheObject<T>>> it = cache.entrySet().iterator();

		Set<T> entriesRemoved = new LinkedHashSet<>();

		while (it.hasNext()) {
			Entry<String, CacheObject<T>> entry = it.next();

			if (shouldRemoveEntry(entry.getValue())) {
				it.remove();
				entriesRemoved.add(entry.getValue().getObject());
			}
		}

		entriesEvictionListener.onEntriesEviction(entriesRemoved);
	}

	private boolean shouldRemoveEntry(CacheObject<T> entry) {
		return entry.getLastAccessed() + ENTRY_TTL < System.currentTimeMillis();
	}

	public void setEntriesEvictionListener(EntriesEvictionListener<T> entriesEvectionListener) {
		this.entriesEvictionListener = entriesEvectionListener;
	}

	public static class CacheObject<T> {

		private T object;
		private long lastAccessed;

		public CacheObject(T object) {
			this.object = object;
			this.lastAccessed = System.currentTimeMillis();
		}

		public T getObject() {
			return object;
		}

		public void setLastAccessed(long lastAccessed) {
			this.lastAccessed = lastAccessed;
		}

		public long getLastAccessed() {
			return lastAccessed;
		}

	}

}
