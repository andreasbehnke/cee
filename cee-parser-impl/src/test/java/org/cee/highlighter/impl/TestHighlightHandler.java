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

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.cee.highlighter.ContentHighlighter.Settings;
import org.cee.highlighter.impl.HighlightHandler;
import org.cee.highlighter.impl.HighlightWriter;
import org.cee.highlighter.impl.TemplateCache;
import org.cee.news.model.TextBlock;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TestHighlightHandler {
	
	@Test
	public void testRewriteUrl() throws MalformedURLException, SAXException {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "", "href", "", "img/pic.jpeg");
		URL documentLocation = new URL("http://www.test.de/home/index.html");
		StringWriter output = new StringWriter();
		Settings settings = new Settings();
		settings.setBaseUrl(documentLocation);
		settings.setRewriteUrls(true);
		HighlightWriter writer = new HighlightWriter(output, new TemplateCache(), settings);
		HighlightHandler handler = new HighlightHandler(new ArrayList<TextBlock>(), new ArrayList<String>(), writer, settings);

		handler.startElement("", "", "a", attributes);
		assertEquals("<a href=\"http://www.test.de/home/img/pic.jpeg\">", output.getBuffer().toString());
	}
}
