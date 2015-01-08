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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.cee.highlighter.ContentHighlighter.Settings;
import org.cee.highlighter.impl.ContentHighlighterImpl;
import org.cee.highlighter.impl.TemplateCache;
import org.cee.parser.ArticleParser;
import org.cee.parser.ArticleReader;
import org.cee.parser.ParserException;
import org.cee.parser.impl.SaxXmlReaderFactory;
import org.cee.parser.impl.TagsoupXmlReaderFactory;
import org.cee.parser.net.ReaderSource;
import org.cee.parser.net.WebResponse;
import org.cee.parser.net.impl.XmlStreamReaderFactory;
import org.cee.store.article.Article;
import org.cee.store.article.ContentExtractionMetaData;
import org.cee.store.article.TextBlock;
import org.cee.store.article.ContentExtractionMetaData.Property;
import org.junit.Test;

public class TestContentHighlighterImpl {
	
	private final static String INPUT_CONTENT_1 = "<html><head></head><body><h1>Title</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";

	private final static String EXPECTED_OUTPUT_CONTENT_1 = "<html><head></head><body><h1>CONTENT STARTTitleCONTENT END</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";
	
	private final static String INPUT_CONTENT_2 = "<html><head><title>Title</title></head><body><p>Text 1</p><p>Text 2</p><p>Text 3</p><p>Text 4</p><p>Text 5</p></body></html>";
	
	private final static String EXPECTED_OUTPUT_CONTENT_2 = "<html><head><title>Title</title></head><body><p>METAICON0 START Text 1 END</p><p>START Text 2 END</p><p>Text 3</p><p>METAICON1 Text 4</p><p>Text 5</p>MB START MB0 MB1 MB END</body></html>";

	@Test
	public void testHighlight() throws IOException, ParserException {
		Reader input = new StringReader(INPUT_CONTENT_1);
		StringWriter output = new StringWriter();
		
		Article article = new Article();
		TextBlock t1 = new TextBlock("Title");
		BitSet containedContent = new BitSet();
		containedContent.set(1);
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property("labels", "TITLE"));
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(0, true, properties, containedContent);
		t1.setMetaData(metaData);
		TextBlock t2 = new TextBlock("This is content");
		containedContent = new BitSet();
		containedContent.set(3);
		properties = new ArrayList<Property>();
		properties.add(new Property("labels", "CONTENT"));
		metaData = new ContentExtractionMetaData(1, false, properties, containedContent);
		t2.setMetaData(metaData);
		article.getContent().add(t1);
		article.getContent().add(t2);
		
		WebResponse response = mock(WebResponse.class);
		when(response.openReaderSource()).thenReturn(new ReaderSource(input, null));
		
		ArticleReader articleReader = mock(ArticleReader.class);
		when(articleReader.readArticle(any(WebResponse.class), any(Article.class), any(ArticleParser.Settings.class))).thenReturn(article);
		
		SaxXmlReaderFactory xmlReaderFactory = new TagsoupXmlReaderFactory();
		
		ContentHighlighterImpl contentHighlighterImpl = new ContentHighlighterImpl(articleReader, xmlReaderFactory, new TemplateCache());
		Settings settings = new Settings();
		settings.setContentBlockTemplate("CONTENT START{CONTENT}CONTENT END");
		settings.setHighlightContentBlock(true);
		contentHighlighterImpl.highlightContent(output, response, article, settings);
		
		assertEquals(EXPECTED_OUTPUT_CONTENT_1, output.getBuffer().toString());
	}
	
	@Test
	public void testMetadata() throws IOException, ParserException {
		Reader input = new StringReader(INPUT_CONTENT_2);
		StringWriter output = new StringWriter();
		
		Article article = new Article();
		TextBlock t1 = new TextBlock("Text 1 and 2");
		BitSet containedContent = new BitSet();
		containedContent.set(2);
		containedContent.set(3);
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property("labels", "content"));
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(0, true, properties, containedContent);
		t1.setMetaData(metaData);
		TextBlock t2 = new TextBlock("Text 4");
		containedContent = new BitSet();
		containedContent.set(5);
		properties = new ArrayList<Property>();
		properties.add(new Property("labels", "boilerplate"));
		metaData = new ContentExtractionMetaData(1, false, properties, containedContent);
		t2.setMetaData(metaData);
		article.getContent().add(t1);
		article.getContent().add(t2);
		
		WebResponse response = mock(WebResponse.class);
		when(response.openReaderSource()).thenReturn(new ReaderSource(input, null));
		
		ArticleReader articleReader = mock(ArticleReader.class);
		when(articleReader.readArticle(any(WebResponse.class), any(Article.class), any(ArticleParser.Settings.class))).thenReturn(article);
		
		SaxXmlReaderFactory xmlReaderFactory = new TagsoupXmlReaderFactory();
		
		ContentHighlighterImpl contentHighlighterImpl = new ContentHighlighterImpl(articleReader, xmlReaderFactory, new TemplateCache(new XmlStreamReaderFactory()));
		Settings settings = new Settings();
		settings.setContentBlockTemplate("START {CONTENT} END");
		settings.setHighlightContentBlock(true);
		settings.setShowBlockMetadata(true);
		settings.setMetadataTemplate("MB{ID} ");
		settings.setMetadataContainerTemplate("MB START {CONTENT}MB END");
		settings.setMetadataIconTemplate("METAICON{ID} ");
		contentHighlighterImpl.highlightContent(output, response, article, settings);
		
		assertEquals(EXPECTED_OUTPUT_CONTENT_2, output.getBuffer().toString());
	}
}
