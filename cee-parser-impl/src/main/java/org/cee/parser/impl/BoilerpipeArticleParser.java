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
import java.util.ArrayList;
import java.util.List;

import org.cee.parser.ArticleParser;
import org.cee.parser.ParserException;
import org.cee.store.article.Article;
import org.cee.store.article.ContentExtractionMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeInput;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ExtractorBase;
import de.l3s.boilerpipe.filters.english.NumWordsRulesClassifier;
import de.l3s.boilerpipe.labels.DefaultLabels;
import de.l3s.boilerpipe.sax.BoilerpipeHTMLContentHandler;

/**
 * Implementation of {@link ArticleParser} using the {@link BoilerpipeInput} of the boilerpipe library.
 */
public class BoilerpipeArticleParser extends XmlReaderProvider implements ArticleParser {
	
	private final static class ArticleExtractor extends ExtractorBase {
	    
		private static final double MAX_LEVENSHTEIN_DISTANCE_HTML_TITLE_MATCH = 0.4;

		private static final double MAX_LEVENSHTEIN_DISTANCE_RSS_TITLE_MATCH = 0.2;

		private final BoilerpipeFilter terminatingBlocksFinder;
	    
	    private final String htmlTitle;
	    
	    private ArticleExtractor(String htmlTitle) {
	    	this.htmlTitle = htmlTitle;
	    	List<String> matches = new ArrayList<String>();
	    	//english stopwords
	    	matches.add("comments");
	    	matches.add("comment");
	    	matches.add("users responded in");
	    	matches.add("please rate this");
	    	matches.add("what you think...");
	    	matches.add("reader views");
	    	matches.add("have your say");
	    	
	    	//german stopwords
	    	matches.add("kommentieren");
	    	matches.add("diesen artikel...");
	    	matches.add("Kommentare zu diesem Artikel");
	    	
	    	terminatingBlocksFinder = new ContainsTextFilter(
	    			DefaultLabels.INDICATES_END_OF_TEXT, 
	    			matches, 
	    			6, 
	    			true);
	    	}

	    @Override
	    public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
	    	BoilerpipeFilter titleFinderRSS = new DocumentTitleMatchFilter(DefaultLabels.TITLE, doc.getTitle(), MAX_LEVENSHTEIN_DISTANCE_RSS_TITLE_MATCH, false);
	    	BoilerpipeFilter titleFinderHTML = new DocumentTitleMatchFilter(DefaultLabels.TITLE, htmlTitle, MAX_LEVENSHTEIN_DISTANCE_HTML_TITLE_MATCH, false);
	    	
	    	boolean changed = terminatingBlocksFinder.process(doc);
	    	boolean foundTitle = titleFinderRSS.process(doc);
	    	foundTitle |= titleFinderHTML.process(doc);
	    	if (!foundTitle) {
	    		LOG.warn("Title not found in content of article {}", doc.getTitle());
	    		return false;
	    	}
	    	changed |= foundTitle; 
	    	changed |= NumWordsRulesClassifier.INSTANCE.process(doc);
	    	changed |= FindContentAfterTitleFilter.INSTANCE.process(doc);
	    	return changed;
	    }
	}
	
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
        	if (!new ArticleExtractor(htmlTitle).process(textDoc)) {
        		LOG.warn("Parsing of article {} failed", article.getLocation());
        		return null;
        	}
            List<org.cee.store.article.TextBlock> content = article.getContent();
            for (TextBlock block : textDoc.getTextBlocks()) {
            	if (!block.isContent() && settings.isFilterContentBlocks()) {
            		continue;
            	}
            	String paragraph = block.getText();
            	org.cee.store.article.TextBlock internalBlock = new org.cee.store.article.TextBlock(paragraph);
            	if (settings.isProvideMetaData()) {
            		ContentExtractionMetaData metaData = ContentExtractionMetaDataFactory.INSTANCE.create(content.size(), block);
            		internalBlock.setMetaData(metaData);
            	}
            	content.add(internalBlock);
            }
            LOG.debug("finished parsing article content of {}, found {} textblocks", article.getLocation(), content.size());
            return article;
        } catch (BoilerpipeProcessingException | SAXException e) {
            throw new ParserException(e);
        }
    }
}
