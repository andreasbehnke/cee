package org.cee.parser;

/*
 * #%L
 * Content Extraction Engine - News Parser
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.news.model.Article;
import org.cee.parser.ArticleParser;
import org.cee.parser.ArticleReader;
import org.cee.parser.ParserException;
import org.cee.parser.ArticleParser.Settings;
import org.cee.parser.net.ReaderSource;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebResponse;
import org.junit.Test;

public class TestArticleReader {

	@Test
	public void testReadArticle() throws ParserException, IOException {
		Article input = new Article();
		input.setLocation("http://any");
		Article output = new Article();
		Reader reader = mock(Reader.class);
		WebClient webClient = mock(WebClient.class);
		when(webClient.openReader(any(URL.class))).thenReturn(reader);
		ArticleParser articleParser = mock(ArticleParser.class);
		when(articleParser.parse(same(reader), same(input), any(Settings.class))).thenReturn(output);
		assertSame(output, new ArticleReader(articleParser).readArticle(webClient, input));
		verify(reader).close();
	}
	
	@Test(expected = IOException.class)
	public void testReadArticleThrowsIOException() throws ParserException, IOException {
		Article input = new Article();
		input.setLocation("http://any");
		Reader reader = mock(Reader.class);
		Settings settings = new Settings();
		WebClient webClient = mock(WebClient.class);
		when(webClient.openReader(any(URL.class))).thenReturn(reader);
		ArticleParser articleParser = mock(ArticleParser.class);
		when(articleParser.parse(reader, input, settings)).thenThrow(new IOException());
		try {
			new ArticleReader(articleParser).readArticle(webClient, input, settings);
		} finally {
			verify(reader).close();
		}
	}
	
	@Test
	public void testReadArtileWebResponse() throws IOException, ParserException {
		Article input = new Article();
		input.setLocation("http://any");
		Article output = new Article();
		Reader reader = mock(Reader.class);
		WebResponse webResponse = mock(WebResponse.class);
		ReaderSource readerSource = mock(ReaderSource.class);
		when(readerSource.getReader()).thenReturn(reader);
		when(webResponse.openReaderSource()).thenReturn(readerSource);
		ArticleParser articleParser = mock(ArticleParser.class);
		when(articleParser.parse(same(reader), same(input), any(Settings.class))).thenReturn(output);
		assertSame(output, new ArticleReader(articleParser).readArticle(webResponse, input));
		verify(reader).close();
	}
}