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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;

import org.cee.BaseWebClientTest;
import org.cee.net.WebClient;
import org.cee.net.WebResponse;
import org.cee.parser.ArticleParser.Settings;
import org.cee.store.article.Article;
import org.junit.Test;

public class TestArticleReader extends BaseWebClientTest {
    
    @Test
	public void testReadArticle() throws ParserException, IOException {
		Article input = new Article();
		input.setLocation("http://any");
		Article output = new Article();
		Reader reader = createReader();
		WebClient webClient = createWebClient(reader);
		ArticleParser articleParser = mock(ArticleParser.class);
		when(articleParser.parse(same(reader), same(input), any(Settings.class))).thenReturn(output);
		assertSame(output, new ArticleReader(articleParser).readArticle(webClient, input));
		verify(reader).close();
	}
	
	@Test(expected = IOException.class)
	public void testReadArticleThrowsIOException() throws ParserException, IOException {
		Article input = new Article();
		input.setLocation("http://any");
		Reader reader = createReader();
		Settings settings = new Settings();
		WebClient webClient = createWebClient(reader);
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
		when(webResponse.openReader()).thenReturn(reader);
		ArticleParser articleParser = mock(ArticleParser.class);
		when(articleParser.parse(same(reader), same(input), any(Settings.class))).thenReturn(output);
		assertSame(output, new ArticleReader(articleParser).readArticle(webResponse, input));
		verify(reader).close();
	}
}
