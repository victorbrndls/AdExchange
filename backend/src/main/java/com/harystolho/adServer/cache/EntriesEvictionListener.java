package com.harystolho.adserver.cache;

import java.util.Set;

public interface EntriesEvictionListener<T> {

	/**
	 * Called after the {@link LRUCache} evits entries
	 * 
	 * @param entries the evicted entries
	 */
	public void onEntriesEviction(Set<T> entries);

}
