package com.harystolho.adexchange.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonResponse {

	private static final JsonNodeFactory factory = new JsonNodeFactory(false);

	private ObjectNode node;

	public JsonResponse() {
		node = new ObjectNode(factory);
	}

	public JsonResponse(String key, Object value) {
		this();
		pair(key, value);
	}

	public JsonResponse pair(String key, Object value) {
		node.putPOJO(key, value);
		return this;
	}

	public static JsonResponse of(String key, Object value) {
		return new JsonResponse(key, value);
	}

	public ObjectNode build() {
		return node;
	}

}
