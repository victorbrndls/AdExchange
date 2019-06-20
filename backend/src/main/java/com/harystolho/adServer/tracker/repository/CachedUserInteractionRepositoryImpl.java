package com.harystolho.adserver.tracker.repository;

import java.util.Optional;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.harystolho.adserver.cache.EntriesEvictionListener;
import com.harystolho.adserver.cache.LRUCache;
import com.harystolho.adserver.tracker.UserInteraction;

@Service(value = "userInteractionCache")
public class CachedUserInteractionRepositoryImpl
		implements UserInteractionRepository, EntriesEvictionListener<UserInteraction> {

	private UserInteractionRepository userInteractionRepository;

	private LRUCache<UserInteraction> cache;

	private CachedUserInteractionRepositoryImpl(UserInteractionRepository userInteractionRepository,
			LRUCache<UserInteraction> cache) {
		this.userInteractionRepository = userInteractionRepository;
		this.cache = cache;
	}

	@Override
	public UserInteraction getByInteractorId(String interactorId) {
		return Optional.ofNullable(cache.get(interactorId)).orElseGet(() -> {
			UserInteraction ui = userInteractionRepository.getByInteractorId(interactorId);

			if (ui != null)
				cache.store(interactorId, ui);

			return ui;
		});
	}

	@Override
	public void save(UserInteraction userInteraction) {
		cache.store(userInteraction.getInteractorId(), userInteraction);
	}

	@Override
	public void onEntriesEviction(Set<UserInteraction> entries) {

	}

	@PreDestroy
	public void destroy() {
		// TODO send the storage entries to db when applications shuts down
	}

}
