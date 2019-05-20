package com.harystolho.adServer.events;

public interface Handler<E> {

	public void onEvent(E event);

}
