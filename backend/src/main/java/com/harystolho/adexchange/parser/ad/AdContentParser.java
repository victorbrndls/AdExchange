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
		charHandler.put('\\', this::parseBackslashChar); // Only 1 backslash
	}

	public void setInput(String input) {
		this.input = input;
	}

	/**
	 * Converts the {input} into the formated result
	 * 
	 * @return
	 */
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

	private void parseBackslashChar(Character c) {
		if (doubleChar(c)) {
			// If there is a new line inside an existing tag, the existing tag should
			// continue after the new line
			String lastTag = currentNode.getTag(); // Save tag to restore later

			appendCurrentNode();

			appendNode(new TagNode("br")); // Add the new line

			this.currentNode = new TagNode(lastTag); // Open the last tag again

			this.pos++; // Skip the second backslash
		} else {
			parseDefaultChar(c);
		}
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
		if (doubleChar(code)) {
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

	/**
	 * @param c
	 * @return <code>true</code> if the current char and the next char are equal to
	 *         {c}
	 */
	private boolean doubleChar(char c) {
		return getCharAt(pos) == c && getCharAt(pos + 1) == c;
	}

	private void appendCurrentNode() {
		if (currentNode != null)
			appendNode(currentNode);
	}

	private void appendNode(TagNode node) {
		this.output.add(node);
	}

}
