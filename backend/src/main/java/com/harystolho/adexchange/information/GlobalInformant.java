package com.harystolho.adexchange.information;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class used to collect information from other classes, the information is used
 * for monitoring purposes
 * 
 * @author Harystolho
 *
 */
@Service
public class GlobalInformant {

	private static final JsonNodeFactory jsonNodeFactory = new JsonNodeFactory(false);

	private List<Visitor> visitors;

	public GlobalInformant() {
		visitors = new ArrayList<>();
	}

	public ArrayNode visitAll() {
		ArrayNode nodes = new ArrayNode(jsonNodeFactory);

		for (Visitor v : visitors) {
			nodes.add(v.visit(this));
		}

		return nodes;
	}

	public void add(Visitor visitor) {
		visitors.add(visitor);
	}

	public ObjectNode defaultObjectNode() {
		return new ObjectNode(jsonNodeFactory);
	}
}
