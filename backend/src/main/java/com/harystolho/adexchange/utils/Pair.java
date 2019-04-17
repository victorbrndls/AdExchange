package com.harystolho.adexchange.utils;

public class Pair<K, V> {

	private K first;
	private V second;

	public Pair(K k, V v) {
		this.first = k;
		this.second = v;
	}

	public static <K, V> Pair<K, V> of(K k, V v) {
		return new Pair<K, V>(k, v);
	}

	public K getFist() {
		return first;
	}

	public void setFirst(K first) {
		this.first = first;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}

}
