package com.harystolho.adexchange.parser.ad;

/**
 * Represents HTML using objects.
 *
 * The HTML code <b>this is a bold text</b> can be transformed into a
 * {@link TagNode}, see the example below.
 * 
 * <pre>
 * TagNode tag = new TagNode("b");
 * tag.setContent("this is a bold text");
 * </pre>
 * 
 * To transform the {@link TagNode} to HTML again use {@link TagNodeWriter}
 * 
 * @author Harystolho
 *
 */
public class TagNode {

	public String tag;
	public String content;

	public TagNode(String tag) {
		this.tag = tag;
		this.content = "";
	}

	public String getTag() {
		return this.tag;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void appendToContent(char c) {
		this.appendToContent(String.valueOf(c));
	}

	public void appendToContent(String s) {
		this.content += s;
	}
}