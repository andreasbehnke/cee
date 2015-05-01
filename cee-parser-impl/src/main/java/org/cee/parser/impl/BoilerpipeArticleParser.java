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
import java.util.List;

import org.cee.parser.ArticleParser;
import org.cee.parser.ParserException;
import org.cee.store.article.Article;
import org.cee.store.article.TextBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.l3s.boilerpipe.BoilerpipeInput;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.sax.BoilerpipeHTMLContentHandler;

/**
 * Implementation of {@link ArticleParser} using the {@link BoilerpipeInput} of the boilerpipe library.
 */
public class BoilerpipeArticleParser extends XmlReaderProvider implements ArticleParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(BoilerpipeArticleParser.class);
    
    public BoilerpipeArticleParser() {}
    
    public BoilerpipeArticleParser(SaxXmlReaderFactory xmlReaderFactory) {
        super(xmlReaderFactory);
    }
    
    @Override
    public Article parse(Reader reader, Article article, Settings settings) throws ParserException, IOException {
    	try {
        	String articleTitel = article.getTitle();
        	LOG.debug("start parsing article content of {}", article.getLocation());
        	BoilerpipeHTMLContentHandler boilerpipeHandler = new BoilerpipeHTMLContentHandler();
        	
        	XMLReader xmlReader = createXmlReader();
        	xmlReader.setContentHandler(boilerpipeHandler);
        	xmlReader.parse(new InputSource(reader));
        	TextDocument textDoc = boilerpipeHandler.toTextDocument();
        	textDoc.setTitle(articleTitel);
        	String htmlTitle = boilerpipeHandler.getTitle();
        	int indexOfHyphen = htmlTitle.indexOf(" - ");
        	if (indexOfHyphen > 0) {
        		htmlTitle = htmlTitle.substring(0, indexOfHyphen);
        	}
        	
        	LOG.debug("extracting main content from {}", article.getLocation());
        	List<TextBlock> content = new BoilerpipeContentExtractor().extractContent(textDoc, htmlTitle, settings);
        	
        	if (content == null) {
        		LOG.warn("Parsing of article {} failed", article.getLocation());
        		return null;
        	}
            
        	article.setContent(content);
        	
            LOG.debug("finished parsing article content of {}, found {} textblocks", article.getLocation(), content.size());
            return article;
        } catch (BoilerpipeProcessingException | SAXException e) {
            throw new ParserException(e);
        }
    }
}
