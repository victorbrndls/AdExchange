package com.harystolho.adServer.events;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EventDispatcher {

	private static final Logger logger = LogManager.getLogger();

	private final BlockingQueue<Event> eventsQueue;
	private final Map<Class<? extends Event>, Handler<? extends Event>> handlers;

	public EventDispatcher() {
		this.handlers = new ConcurrentHashMap<>();
		this.eventsQueue = new ArrayBlockingQueue<Event>(32); // TODO make sure this queue doesn't overfill
	}

	public <E extends Event> void registerHandler(Class<E> clazz, Handler<E> handler) {
		handlers.put(clazz, handler);
	}

	public <E extends Event> void dispatch(E event) {
		try {
			eventsQueue.put(event);
		} catch (InterruptedException e1) {
			logger.throwing(e1);
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends Event> void dispatchEventsInQueue() {
		try {
			Event event = eventsQueue.take();

			Handler<E> handler = (Handler<E>) handlers.get(event.getClass());

			handler.onEvent((E) event);
		} catch (InterruptedException e1) {
			logger.throwing(e1);
		}

	}
}
