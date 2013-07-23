package com.cee.news.language.impl;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import org.ccil.cowan.tagsoup.Parser;
import org.junit.Test;

import com.cee.news.highlighter.ContentHighlighter.Settings;
import com.cee.news.highlighter.impl.ContentHighlighterImpl;
import com.cee.news.model.Article;
import com.cee.news.model.TextBlock;
import com.cee.news.model.TextBlock.ContentExtractionMetaData;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.impl.ArticleReader;
import com.cee.news.parser.net.ReaderSource;
import com.cee.news.parser.net.WebResponse;

public class TestContentHighlighterImpl {
	
	private final static String INPUT_CONTENT = "<html><head></head><body><h1>Title</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";

	private final static String EXPECTED_OUTPUT_CONTENT = "<html><head></head><body><h1>CONTENT STARTTitleCONTENT END</h1><p>This is not a textblock</p><p>This is a textblock but no content</p></body></html>";
	
	@Test
	public void testHighlight() throws IOException, ParserException {
		Reader input = new StringReader(INPUT_CONTENT);
		StringWriter output = new StringWriter();
		
		Article article = new Article();
		TextBlock t1 = new TextBlock("Title");
		BitSet containedContent = new BitSet();
		containedContent.set(1);
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(true, "label:TITLE", containedContent);
		t1.setMetaData(metaData);
		TextBlock t2 = new TextBlock("This is content");
		containedContent = new BitSet();
		containedContent.set(3);
		metaData = new ContentExtractionMetaData(false, "label:CONTENT", containedContent);
		t2.setMetaData(metaData);
		article.getContent().add(t1);
		article.getContent().add(t2);
		
		WebResponse response = mock(WebResponse.class);
		when(response.openReaderSource()).thenReturn(new ReaderSource(input, null));
		
		ArticleReader articleReader = mock(ArticleReader.class);
		when(articleReader.readArticle(any(WebResponse.class), any(Article.class), any(ArticleParser.Settings.class))).thenReturn(article);
		
		Parser xmlReader = new Parser();
		
		ContentHighlighterImpl contentHighlighterImpl = new ContentHighlighterImpl(articleReader, xmlReader);
		Settings settings = new Settings();
		settings.setContentBlockStart("CONTENT START");
		settings.setContentBlockEnd("CONTENT END");
		contentHighlighterImpl.highlightContent(output, response, article, settings);
		
		assertEquals(EXPECTED_OUTPUT_CONTENT, output.getBuffer().toString());
	}
	
}
