package com.harystolho.adexchange.parser.ad;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class AdContentParserTest {

	@Test
	public void parseInput_NoCodes() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("This is a string used to test this method.");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getContent(), "This is a string used to test this method.");
	}

	@Test
	public void parseInput_OneCode() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("**this is a big phrase**");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getTag(), "b");
		assertEquals(output.get(0).getContent(), "this is a big phrase");
	}

	@Test
	public void parseInput_OneCharOfTwoCharCode() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("**java is a *programming language**");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getTag(), "b");
		assertEquals(output.get(0).getContent(), "java is a *programming language");
	}

	@Test
	public void parseInput_OnlyInitiateCode() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("**where is the end?");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getTag(), "b");
		assertEquals(output.get(0).getContent(), "where is the end?");
	}

	@Test
	public void parseInput_TwoCodes() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("**bold text**__italic__");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getTag(), "b");
		assertEquals(output.get(0).getContent(), "bold text");

		assertEquals(output.get(1).getTag(), "i");
		assertEquals(output.get(1).getContent(), "italic");
	}

	@Test
	public void parseInput_ThreeCodes() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("**text** some spacer __end__");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getTag(), "b");
		assertEquals(output.get(0).getContent(), "text");

		assertEquals(output.get(1).getTag(), "span");
		assertEquals(output.get(1).getContent(), " some spacer ");

		assertEquals(output.get(2).getTag(), "i");
		assertEquals(output.get(2).getContent(), "end");
	}

	@Test
	public void parseInput_SomeCodeInDifferentLocations() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("this **is** the **sheep**");

		List<TagNode> output = parser.parse();

		assertEquals(output.get(0).getContent(), "this ");

		assertEquals(output.get(1).getTag(), "b");
		assertEquals(output.get(1).getContent(), "is");

		assertEquals(output.get(2).getContent(), " the ");

		assertEquals(output.get(3).getTag(), "b");
		assertEquals(output.get(3).getContent(), "sheep");
	}
	
	
	@Test
	public void parseInput_WithNewLine() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("this\\\\new"); // Only 2 backslashes

		List<TagNode> output = parser.parse();

		assertEquals("this", output.get(0).getContent());

		assertEquals("br" ,output.get(1).getTag());

		assertEquals("new", output.get(2).getContent());
	}
	
	@Test
	public void parseInput_WithSingleBackslash_ShouldNotCreateNewLine() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("this\\new"); // Only 1 backslash

		List<TagNode> output = parser.parse();

		assertEquals("this\\new", output.get(0).getContent());
	}
	
	@Test
	public void parseInput_useNewLineInsideBold() {
		AdContentParser parser = new AdContentParser();
		parser.setInput("text **bold\\\\green** end"); // Only 2 backslashes

		List<TagNode> output = parser.parse();

		assertEquals("text ", output.get(0).getContent());

		assertEquals("b" ,output.get(1).getTag());
		assertEquals("bold" , output.get(1).getContent());
		
		assertEquals("br", output.get(2).getTag());
		
		assertEquals("b", output.get(3).getTag());
		assertEquals("green" , output.get(3).getContent());
		
		assertEquals("span", output.get(4).getTag());
		assertEquals(" end", output.get(4).getContent());
	}
}
