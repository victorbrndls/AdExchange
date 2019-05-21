package com.harystolho.adServer.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class EventDispatcher {

	private final Map<Class<? extends Event>, Handler<? extends Event>> handlers;

	public EventDispatcher() {
		this.handlers = new ConcurrentHashMap<>();
	}

	public <E extends Event> void registerHandler(Class<E> clazz, Handler<E> handler) {
		handlers.put(clazz, handler);
	}

	@SuppressWarnings("unchecked")
	public <E extends Event> void dispatch(E event) {
		Handler<E> handler = (Handler<E>) handlers.get(event.getClass());
		handler.onEvent(event);
	}
}
