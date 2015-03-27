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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.cee.highlighter.impl.TemplateCache;
import org.cee.net.impl.XmlStreamReaderFactory;
import org.junit.Test;

public class TestTemplateCache {
	
	@Test
	public void testGetTemplateContent() {
		TemplateCache templateCache = new TemplateCache();
		String templateResource = "Simple Resource";
		String result = templateCache.getTemplateContent(templateResource);
		assertEquals(templateResource, result);
	}

	@Test
	public void testGetTemplateContentResource() {
		TemplateCache templateCache = new TemplateCache(new XmlStreamReaderFactory());
		String templateResource = "classpath:/org/cee/highlighter/impl/testTemplateCache.txt";
		String result = templateCache.getTemplateContent(templateResource);
		assertEquals("Hello World!", result);
	}

	@Test
	public void testFillTemplate() {
		TemplateCache templateCache = new TemplateCache();
		String templateResource = "A-{B}-{C}-{D}";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("B", "C");
		parameters.put("C", "D");
		parameters.put("D", "C");
		String result = templateCache.fillTemplate(templateResource, parameters);
		
		assertEquals("A-C-D-C", result);
	}
}
