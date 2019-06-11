package com.harystolho.adexchange.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlDocumentReader {

	private static final Logger logger = LogManager.getLogger();

	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	public static Document parseStringToXML(String xmlAsString) {
		DocumentBuilder db = null;

		try {
			db = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.catching(e);
			return null;
		}

		Document xml = null;

		try {
			xml = db.parse(new InputSource(new ByteArrayInputStream(xmlAsString.getBytes())));
		} catch (SAXException | IOException e) {
			logger.catching(e);
			return null;
		}

		// https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		xml.getDocumentElement().normalize();

		return xml;
	}

}
