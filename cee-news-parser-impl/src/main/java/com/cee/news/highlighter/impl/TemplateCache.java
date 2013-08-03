package com.cee.news.highlighter.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.cee.news.parser.net.impl.ReaderFactory;

public class TemplateCache {

	private final Map<String, String> templates = new HashMap<String, String>();
	
	private ReaderFactory readerFactory;
	
	public void setReaderFactory(ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}
	
	private String readContent(String templateResource) {
		Reader reader = null;
		InputStream input = null;
		try {
			input = getClass().getClassLoader().getResourceAsStream(templateResource);
			if (input == null) {
				return templateResource;
			} else {
				reader = readerFactory.createReader(input, "text/html", "UTF-8").getReader();
				return IOUtils.toString(reader);
			}
		} catch (IOException e) {
            throw new RuntimeException("Could not read content resource for " + templateResource, e);
        } finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(input);
		}
	}

	public String getTemplateContent(String templateResource) {
		String content = templates.get(templateResource);
		if (content == null) {
			content = readContent(templateResource);
			templates.put(templateResource, content);
		}
		return content;
	}
	
	private String createPlaceholder(String parameterName) {
		return "{" + parameterName + "}";
	}
	
	public String fillTemplate(String templateResource, Map<String, String> parameters) {
		String content = getTemplateContent(templateResource);
		for (Map.Entry<String, String> parameterEntry : parameters.entrySet()) {
			String placeholder = createPlaceholder(parameterEntry.getKey());
			String value = parameterEntry.getValue();
			content = content.replace(placeholder, value);
        }
		return content;
	}
}