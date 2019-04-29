package com.harystolho.adexchange.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonResponse {

	private static final JsonNodeFactory factory = new JsonNodeFactory(false);

	private ObjectNode node;

	public JsonResponse() {
		node = new ObjectNode(factory);
	}

	public JsonResponse(String key, String value) {
		this();
		pair(key, value);
	}

	public JsonResponse pair(String key, String value) {
		node.put(key, value);
		return this;
	}

	public ObjectNode build() {
		return node;
	}

}
