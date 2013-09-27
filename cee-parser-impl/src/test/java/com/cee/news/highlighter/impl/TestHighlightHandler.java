package com.cee.news.highlighter.impl;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.model.TextBlock;

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
