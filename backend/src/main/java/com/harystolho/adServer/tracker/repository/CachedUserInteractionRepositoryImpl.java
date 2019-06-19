package com.harystolho.adserver.tracker.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.harystolho.adserver.services.CacheService;
import com.harystolho.adserver.tracker.UserInteraction;

@Service(value = "userInteractionCache")
public class CachedUserInteractionRepositoryImpl implements UserInteractionRepository {

	private UserInteractionRepository userInteractionRepository;
	private CacheService<UserInteraction> cache;

	private CachedUserInteractionRepositoryImpl(UserInteractionRepository userInteractionRepository,
			CacheService<UserInteraction> cache) {
		this.userInteractionRepository = userInteractionRepository;
		this.cache = cache;
	}

	@Override
	public UserInteraction getByInteractorId(String interactorId) {
		return Optional.of(cache.get(interactorId)).orElseGet(() -> {
			UserInteraction ui = userInteractionRepository.getByInteractorId(interactorId);

			if (ui != null)
				storeOnCache(ui);

			return ui;
		});
	}

	@Override
	public UserInteraction save(UserInteraction userInteraction) {
		storeOnCache(userInteraction);

		return userInteractionRepository.save(userInteraction);
	}

	private void storeOnCache(UserInteraction ui) {
		cache.store(ui.getInteractorId(), ui, Duration.ofMinutes(1));
	}
}
