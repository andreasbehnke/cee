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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.highlighter.ContentHighlighter;
import org.cee.parser.ArticleParser;
import org.cee.parser.ArticleReader;
import org.cee.parser.ParserException;
import org.cee.parser.impl.SaxXmlReaderFactory;
import org.cee.parser.impl.XmlReaderProvider;
import org.cee.parser.net.WebResponse;
import org.cee.store.article.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class ContentHighlighterImpl extends XmlReaderProvider implements ContentHighlighter {
	
	private final static Logger LOG = LoggerFactory.getLogger(ContentHighlighterImpl.class);
	
	private final static String ISSUE_COULD_NOT_PARSE_ARTICLE = "Article could not be parsed";
	
	private ArticleReader articleReader;
	
	private TemplateCache templateCache;
	
	public ContentHighlighterImpl() {}
	
	public ContentHighlighterImpl(ArticleReader articleReader, SaxXmlReaderFactory xmlReaderFactory, TemplateCache templateCache) {
		super(xmlReaderFactory);
	    this.articleReader = articleReader;
	    this.templateCache = templateCache;
    }
	
	public void setArticleReader(ArticleReader articleReader) {
	    this.articleReader = articleReader;
    }
	
	public void setTemplateCache(TemplateCache templateCache) {
	    this.templateCache = templateCache;
    }

	@Override
    public void highlightContent(Writer output, WebResponse response, Article article,  Settings settings) throws MalformedURLException, IOException, ParserException {
	    List<String> issues = new ArrayList<String>();
	    ArticleParser.Settings parserSettings = new ArticleParser.Settings();
	    parserSettings.setProvideMetaData(true);
	    parserSettings.setFilterContentBlocks(false);
	    try {
	    	article = articleReader.readArticle(response, article, parserSettings);
	    } catch(ParserException pe) {
	    	LOG.warn(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    	issues.add(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    }
		XMLReader xmlReader = createXmlReader();
	    HighlightWriter writer = new HighlightWriter(output, templateCache, settings);
	    xmlReader.setContentHandler(new HighlightHandler(article.getContent(), issues, writer, settings));
	    Reader reader = response.openReaderSource().getReader();
	    try {
	    	xmlReader.parse(new InputSource(reader));
	    } catch (SAXException e) {
	        throw new ParserException(e);
        } finally {
	    	IOUtils.closeQuietly(reader);
	    }
    }
}
