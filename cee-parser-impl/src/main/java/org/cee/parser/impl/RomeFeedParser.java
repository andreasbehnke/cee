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


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.parser.FeedParser;
import org.cee.parser.ParserException;
import org.cee.store.article.Article;
import org.cee.store.site.Feed;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SAXBuilder;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.WireFeedParser;
import com.sun.syndication.io.impl.FeedParsers;
import com.sun.syndication.io.impl.XmlFixerReader;

import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.sax.BoilerpipeHTMLContentHandler;

public class RomeFeedParser extends WireFeedInput implements FeedParser {

	private final static Logger LOG = LoggerFactory.getLogger(RomeFeedParser.class);

	private static FeedParsers feedParsers = new FeedParsers();

	private XmlReaderProvider xmlReaderProvider;

	public RomeFeedParser() {
	}

	public RomeFeedParser(SaxXmlReaderFactory xmlReaderFactory) {
		xmlReaderProvider = new XmlReaderProvider(xmlReaderFactory);
	}
	
	public void setXmlReaderFactory(SaxXmlReaderFactory xmlReaderFactory) {
		xmlReaderProvider = new XmlReaderProvider(xmlReaderFactory);
	}

	@SuppressWarnings("serial")
	private class EmptyElementFilter implements Filter {

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof Element) {
				Element element = (Element) obj;
				int attributeCount = element.getAttributes().size();
				int contentCount = element.getContentSize();
				boolean isEmpty = element.getTextTrim() == null || element.getTextTrim().isEmpty();
				if (isEmpty && attributeCount == 0 && contentCount == 0) {
					return true;
				}
			}
			return false;
		}

	}

	private void removeEmptyElements(Element element) {
		element.removeContent(new EmptyElementFilter());
		for (Object obj : element.getContent()) {
			if (obj instanceof Element) {
				removeEmptyElements((Element) obj);
			}
		}
	}

	private Document openDocument(final Reader reader) throws JDOMException, IOException {
		Reader input = null;
		try {
			SAXBuilder saxBuilder = createSAXBuilder();
			input = new XmlFixerReader(reader);
			Document document = saxBuilder.build(new InputSource(input));
			removeEmptyElements(document.getRootElement());
			return document;
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	private SyndFeed readFeed(final Reader reader) throws IllegalArgumentException, FeedException, IOException, JDOMException {
		return new SyndFeedInput().build(openDocument(reader));
	}

	public boolean isSupportedFeed(Document document) throws IOException {
		WireFeedParser parser = feedParsers.getParserFor(document);
		return parser != null;
	}

	@Override
	public Feed parse(Reader reader, URL location) throws ParserException, IOException {
		try {
			Document document = openDocument(reader);
			if (!isSupportedFeed(document)) {
				throw new ParserException("Unsupported feed found at " + location);
			}
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed syndFeed = input.build(document);
			Feed feed = new Feed(location.toExternalForm(), syndFeed.getTitle());
			String language = syndFeed.getLanguage();
			feed.setLanguage(language);
			return feed;
		} catch (JDOMException | IllegalArgumentException | FeedException e) {
			throw new ParserException("Could not parse feed " + location, e);
		}
	}

	private String extractTextFromHtml(String html) throws IOException, SAXException {
		Reader reader = null;
		try {
			XMLReader xmlReader = xmlReaderProvider.createXmlReader();
			BoilerpipeHTMLContentHandler boilerpipeHandler = new BoilerpipeHTMLContentHandler();
			xmlReader.setContentHandler(boilerpipeHandler);
			reader = new StringReader(html);
			InputSource is = new InputSource(reader);
			xmlReader.parse(is);
			TextDocument textDoc = boilerpipeHandler.toTextDocument();
			String text = textDoc.getText(true, true);
			if (text != null) {
				text = text.trim();
			}
			return text;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private boolean isHtml(SyndContent content) {
		String contentType = content.getType();
		if (contentType == null) {
			return false;
		}
		contentType = contentType.toLowerCase();
		return contentType.contains("html");
	}

	private String extractShortText(SyndEntry entry) throws ParserException, IOException {
		SyndContent content = entry.getDescription();
		if (content == null) {
			if (entry.getContents().size() > 0) {
				content = (SyndContent) entry.getContents().get(0);
			} else {
				return null;
			}
		}
		String contentValue = content.getValue();
		if (isHtml(content)) {
			try {
				return extractTextFromHtml(contentValue);
			} catch (SAXException e) {
				throw new ParserException("Could not extract text from HTML short text", e);
			}
		} else {
			return contentValue;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Article> readArticles(final Reader reader, final URL feedLocation) throws ParserException, IOException {
		List<Article> articles = new ArrayList<Article>();
		SyndFeed syndFeed = null;
		try {
			syndFeed = readFeed(reader);
		} catch (JDOMException | IllegalArgumentException | FeedException e) {
			throw new ParserException("Could not parse feed", e);
		}

		for (SyndEntry entry : (List<SyndEntry>) syndFeed.getEntries()) {
			Article article = new Article();
			article.setTitle(entry.getTitle());
			article.setShortText(extractShortText(entry));
			String link = entry.getLink();
			String id = entry.getUri();
			if (id == null) {
				id = link;
			}
			article.setExternalId(id);
			try {
				URL articleUrl = new URL(feedLocation, link);// check for well
				                                             // formed URL
				article.setLocation(articleUrl.toExternalForm());
			} catch (MalformedURLException e) {
				LOG.warn("The feed {} contains article with invalid URL {}", feedLocation, link);
				continue;
			}
			if (entry.getPublishedDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.setTime(entry.getPublishedDate());
				article.setPublishedDate(cal);
			} else {
				article.setPublishedDate(Calendar.getInstance());
			}
			articles.add(article);
		}

		return articles;
	}
}
