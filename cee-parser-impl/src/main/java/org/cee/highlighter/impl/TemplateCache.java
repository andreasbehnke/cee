package org.cee.highlighter.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.cee.parser.net.impl.ReaderFactory;

public class TemplateCache {
	
	private final static String RESOURCE_SCHEME = "classpath:";

	private final Map<String, String> templates = new HashMap<String, String>();
	
	private ReaderFactory readerFactory;
	
	public TemplateCache() {}
	
	public TemplateCache(ReaderFactory readerFactory) {
	    this.readerFactory = readerFactory;
    }

	public void setReaderFactory(ReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}
	
    private String readContent(String templateResource) {
        try (InputStream input = getClass().getResourceAsStream(templateResource);
                Reader reader = readerFactory.createReader(input, "text/html", "UTF-8").getReader()) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            throw new RuntimeException("Could not read content resource for " + templateResource, e);
        }
    }

	public String getTemplateContent(String templateResource) {
		if (templateResource == null) {
			throw new IllegalArgumentException("Paramter templateResource must not be null");
		}
		if (templateResource.startsWith(RESOURCE_SCHEME)) {
			String resourcePath = templateResource.substring(RESOURCE_SCHEME.length());
			String content = templates.get(resourcePath);
			if (content == null) {
				content = readContent(resourcePath);
				templates.put(resourcePath, content);
			}
			return content;
		} else {
			return templateResource;
		}
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