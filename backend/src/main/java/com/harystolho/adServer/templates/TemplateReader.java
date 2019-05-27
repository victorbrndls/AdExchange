package com.harystolho.adserver.templates;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class TemplateReader {

	private static final Logger logger = LogManager.getLogger();

	private final String templateFilePath = "classpath:templates";

	// Template name, Template
	private Map<String, String> templates;

	public TemplateReader() {
		templates = new HashMap<>();

		loadTemplates();
	}

	/*
	 * Loads templates from file. This method can be called to reload the templates
	 */
	private void loadTemplates() {
		templates.clear();

		buildTemplates(readFileLines());
	}

	public String getTemplate(String name) {
		return templates.get(name.toUpperCase());
	}

	protected void buildTemplates(List<String> lines) {
		String templateName = "";
		String template = "";

		for (String line : lines) {
			if (line.isEmpty()) { // End of template
				addTemplate(templateName, template);

				templateName = "";
				template = "";
			} else if (line.charAt(0) == '#') { // This is the template name
				templateName = line.substring(1).trim(); // Remote the '#' and white spaces
			} else {
				template += line.trim();
			}
		}

		if (!template.equals("")) { // Reached the last line but has not saved the template
			addTemplate(templateName, template);
		}
	}

	private void addTemplate(String name, String template) {
		logger.info("Add template [{}]", name);
		templates.put(name, template);
	}

	private List<String> readFileLines() {
		File templatesFile = loadTemplateFile(templateFilePath);

		if (templatesFile == null)
			return new ArrayList<>();

		try {
			return Files.readAllLines(templatesFile.toPath());
		} catch (IOException e) {
			logger.throwing(e);
		}

		return new ArrayList<>();
	}

	private File loadTemplateFile(String filePath) {
		try {
			return ResourceUtils.getFile(filePath);
		} catch (FileNotFoundException e) {
			logger.throwing(e);
		}

		return null;
	}

}
