package com.cee.news.highlighter.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestTemplateCache {

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
