package com.harystolho.adexchange.parser.ad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AdContentParser {

	private String input;
	private List<TagNode> output;
	private int pos;
	private TagNode currentNode;

	// Maps a char to a method that handles that char
	private Map<Character, Consumer<Character>> charHandler;

	public AdContentParser() {
		this.pos = 0;
		this.output = new ArrayList<>();
		this.charHandler = new HashMap<>();

		charHandler.put('*', this::parseAsteriskChar);
		charHandler.put('_', this::parseUnderscoreChar);
	}

	public void setInput(String input) {
		this.input = input;
	}

	public List<TagNode> parse() {
		int inputLength = this.input.length();

		while (this.pos < inputLength) {
			char currentChar = this.input.charAt(this.pos);

			getCharHandler(currentChar).accept(currentChar);

			this.pos++;
		}

		appendCurrentNode();

		return this.output;
	}

	private Consumer<Character> getCharHandler(char c) {
		return this.charHandler.getOrDefault(c, this::parseDefaultChar);
	}

	private void parseDefaultChar(Character c) {
		if (currentNode == null)
			createDefaultCurrentNode();

		this.currentNode.appendToContent(c);
	}

	private void createDefaultCurrentNode() {
		this.currentNode = new TagNode("span");
	}

	private void parseAsteriskChar(Character c) {
		parseTwoCharCode('*', "b");
	}

	private void parseUnderscoreChar(Character c) {
		parseTwoCharCode('_', "i");
	}

	/**
	 * Most of the time 2 chars are used to format some text, for example the '**'
	 * code is used to make the text bold, the '__' to make the text italic. This
	 * method abstracts the common functionalities in them
	 * 
	 * @param code
	 * @param htmlTag
	 */
	private void parseTwoCharCode(char code, String htmlTag) {
		// If there are 2 chars that are equal next to each other
		if (getCharAt(pos) == code && getCharAt(pos + 1) == code) {
			appendCurrentNode();

			if (this.currentNode == null || (this.currentNode != null && !this.currentNode.getTag().equals(htmlTag))) {
				this.currentNode = new TagNode(htmlTag);
			} else if (this.currentNode.getTag().equals(htmlTag)) {
				this.currentNode = null;
			}

			this.pos++;
		} else {
			parseDefaultChar(code);
		}
	}

	private char getCharAt(int index) {
		return this.input.charAt(index);
	}

	private void appendCurrentNode() {
		if (currentNode != null) {
			this.output.add(currentNode);
		}
	}

}
