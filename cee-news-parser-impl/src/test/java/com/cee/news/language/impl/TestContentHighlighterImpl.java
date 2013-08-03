package com.cee.news.language.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import org.junit.Test;

import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.highlighter.impl.ContentHighlighterImpl;
import com.cee.news.highlighter.impl.TemplateCache;
import com.cee.news.model.Article;
import com.cee.news.model.TextBlock;
import com.cee.news.model.TextBlock.ContentExtractionMetaData;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.impl.ArticleReader;
import com.cee.news.parser.impl.SaxXmlReaderFactory;
import com.cee.news.parser.impl.TagsoupXmlReaderFactory;
import com.cee.news.parser.net.ReaderSource;
import com.cee.news.parser.net.WebResponse;

public class TestContentHighlighterImpl {
	
	private final static String INPUT_CONTENT_1 = "<html><head></head><body><h1>Title</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";

	private final static String EXPECTED_OUTPUT_CONTENT_1 = "<html><head></head><body><h1>CONTENT STARTTitleCONTENT END</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";
	
	private final static String INPUT_CONTENT_2 = "<html><head><title>Title</title></head><body><p>Text 1</p><p>Text 2</p><p>Text 3</p><p>Text 4</p><p>Text 5</p></body></html>";
	
	private final static String EXPECTED_OUTPUT_CONTENT_2 = "<html><head><title>Title</title></head><body><p>METAICON0 START Text 1 END</p><p>START Text 2 END</p><p>Text 3</p><p>METAICON1 Text 4</p><p>Text 5</p></body></html>";

	@Test
	public void testHighlight() throws IOException, ParserException {
		Reader input = new StringReader(INPUT_CONTENT_1);
		StringWriter output = new StringWriter();
		
		Article article = new Article();
		TextBlock t1 = new TextBlock("Title");
		BitSet containedContent = new BitSet();
		containedContent.set(1);
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(0, true, "label:TITLE", containedContent);
		t1.setMetaData(metaData);
		TextBlock t2 = new TextBlock("This is content");
		containedContent = new BitSet();
		containedContent.set(3);
		metaData = new ContentExtractionMetaData(1, false, "label:CONTENT", containedContent);
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
	public void testMetadataIcon() throws IOException, ParserException {
		Reader input = new StringReader(INPUT_CONTENT_2);
		StringWriter output = new StringWriter();
		
		Article article = new Article();
		TextBlock t1 = new TextBlock("Text 1 and 2");
		BitSet containedContent = new BitSet();
		containedContent.set(2);
		containedContent.set(3);
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(0, true, "label:content", containedContent);
		t1.setMetaData(metaData);
		TextBlock t2 = new TextBlock("Text 4");
		containedContent = new BitSet();
		containedContent.set(5);
		metaData = new ContentExtractionMetaData(1, false, "label:boilerplate", containedContent);
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
		settings.setContentBlockTemplate("START {CONTENT} END");
		settings.setHighlightContentBlock(true);
		settings.setShowBlockMetadata(true);
		settings.setMetadataIconTemplate("METAICON{ID} ");
		contentHighlighterImpl.highlightContent(output, response, article, settings);
		
		assertEquals(EXPECTED_OUTPUT_CONTENT_2, output.getBuffer().toString());
	}
}
