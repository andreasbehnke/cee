package com.cee.news.highlighter.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cee.news.parser.net.impl.XmlStreamReaderFactory;

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
		String templateResource = "classpath:/com/cee/news/highlighter/impl/testTemplateCache.txt";
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
