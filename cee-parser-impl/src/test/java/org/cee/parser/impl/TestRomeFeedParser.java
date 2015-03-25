package org.cee.parser.impl;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.parser.FeedParser;
import org.cee.parser.ParserException;
import org.cee.parser.impl.RomeFeedParser;
import org.cee.parser.impl.TagsoupXmlReaderFactory;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.impl.DefaultHttpClientFactory;
import org.cee.parser.net.impl.DefaultWebClient;
import org.cee.parser.net.impl.XmlStreamReaderFactory;
import org.cee.store.article.Article;
import org.junit.Test;

public class TestRomeFeedParser {
	
	private FeedParser createFeedParser() {
		return new RomeFeedParser(new TagsoupXmlReaderFactory());
	}
	
	private Reader openReader(URL location) throws IOException {
		WebClient webClient = new DefaultWebClient(new DefaultHttpClientFactory(), new XmlStreamReaderFactory());
        return webClient.openWebResponse(location).openReader();
	}
	
	private List<Article> readArticles(URL location) throws IOException, ParserException {
		FeedParser parser = createFeedParser();
		Reader reader = openReader(location);
		try {
			return parser.readArticles(reader, location);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	@Test
    public void testReadArticles() throws ParserException, IOException {
		List<Article> articles = readArticles(getClass().getResource("spiegelNachrichten.rss"));
        
        assertEquals(7, articles.size());
        Article article = articles.get(0);
        assertEquals("Krieg in Afghanistan: Nato startet Awacs-Mission ohne Deutschland", article.getTitle());
        assertEquals("http://www.spiegel.de/politik/ausland/0,1518,738566,00.html#ref=rss", article.getLocation().toString());
        assertTrue(article.getShortText().startsWith(
                "Die Nato beginnt die"));
        assertEquals(6, article.getPublishedDate().get(Calendar.HOUR));
        assertEquals("http://www.spiegel.de/politik/ausland/0,1518,738566,00.html", article.getExternalId());

    }
	

    @Test
    public void testReadHtmlShortText() throws ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("feedWithHtmlDescription.rss"));
        
        Article article = articles.get(0);
        assertEquals(
        		"Das Hochwasser stellt Teile Deutschlands weiter vor riesige Probleme. Mittlerweile rollt die Flutwelle gen Norddeutschland. Im Süden und Osten hinterlässt sie Zerstörung. In Sachsen-Anhalt kam ein weiterer Mensch ums Leben.",
        		article.getShortText());
    }
    
    @Test
    public void testReadArticlesRegressionIssue143() throws MalformedURLException, ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("issue143.xml"));
        assertEquals(42, articles.size());
    }
    
    @Test
    public void testReadArticlesRegressionIssue297() throws MalformedURLException, ParserException, IOException {
    	List<Article> articles = readArticles(getClass().getResource("issue297.xml"));
        assertNotNull(articles.get(0).getShortText());
        assertTrue(articles.get(0).getShortText().contains("Dresden (dpa/sn) - Dresden will"));
    }
}
