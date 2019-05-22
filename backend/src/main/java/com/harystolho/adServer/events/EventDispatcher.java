package com.harystolho.adServer.events;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class EventDispatcher {

	private final Map<Class<? extends Event>, Handler<? extends Event>> handlers;

	private TaskExecutor taskExecutor;

	public EventDispatcher(@Qualifier("ae_taskExecutor") TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;

		this.handlers = new ConcurrentHashMap<>();
	}

	public <E extends Event> void registerHandler(Class<E> clazz, Handler<E> handler) {
		handlers.put(clazz, handler);
	}

	@SuppressWarnings("unchecked")
	public <E extends Event> void dispatch(E event) {
		// Dispath the task to another thread so it doesn't block the caller
		taskExecutor.execute(() -> {
			Handler<E> handler = (Handler<E>) handlers.get(event.getClass());

			handler.onEvent((E) event);
		});
	}

}
