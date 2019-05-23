package com.harystolho.adexchange.events;

public interface Handler<E> {

	public void onEvent(E event);

}
