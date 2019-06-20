package com.harystolho.adserver.tracker.repository;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.harystolho.adexchange.utils.Pair;
import com.harystolho.adserver.tracker.UserInteraction;

@Service(value = "userInteractionCache")
public class CachedUserInteractionRepositoryImpl implements UserInteractionRepository {

	private static final int CLEAN_UP_DELAY = 1000 * 60 * 2; // 2 Minutes
	private static final int MAX_ENTRY_TIME = 1000 * 60 * 5; // 5 Minutes

	private UserInteractionRepository userInteractionRepository;

	private Map<String, Pair<UserInteraction, Long>> storage;

	private CachedUserInteractionRepositoryImpl(UserInteractionRepository userInteractionRepository) {
		this.userInteractionRepository = userInteractionRepository;
		this.storage = new ConcurrentHashMap<>();
	}

	@Override
	public UserInteraction getByInteractorId(String interactorId) {
		return Optional.ofNullable(getFromStorage(interactorId)).orElseGet(() -> {
			UserInteraction ui = userInteractionRepository.getByInteractorId(interactorId);

			if (ui != null)
				storeOnCache(ui);

			return ui;
		});
	}

	private UserInteraction getFromStorage(String interactorId) {
		Pair<UserInteraction, Long> pair = storage.get(interactorId);

		if (pair == null)
			return null;

		pair.setSecond(System.currentTimeMillis()); // Update last used time

		return pair.getFirst();
	}

	@Override
	public void save(UserInteraction userInteraction) {
		storeOnCache(userInteraction);
	}

	private void storeOnCache(UserInteraction ui) {
		storage.put(ui.getInteractorId(), Pair.of(ui, System.currentTimeMillis()));
	}

	@Scheduled(fixedDelay = CLEAN_UP_DELAY)
	public void removeLeastRecentlyUsed() {
		Iterator<Entry<String, Pair<UserInteraction, Long>>> it = storage.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Pair<UserInteraction, Long>> entry = it.next();

			if (entry.getValue().getSecond() + MAX_ENTRY_TIME > System.currentTimeMillis()) {
				System.out.println("removing: " + entry.getValue().getFirst().getInteractorId());
				it.remove();
			}
		}

	}
}
