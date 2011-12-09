package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.cee.news.model.Article;
import com.cee.news.parser.ArticleFilter;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;

import de.l3s.boilerpipe.BoilerpipeInput;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.sax.BoilerpipeHTMLContentHandler;

/**
 * Implementation of {@link ArticleParser} using the {@link BoilerpipeInput} of the boilerpipe library.
 */
public class BoilerpipeArticleParser implements ArticleParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(BoilerpipeArticleParser.class);
    
    private XMLReader xmlReader;
    
    private WebClient webClient;
    
    private List<ArticleFilter> filters;

    public BoilerpipeArticleParser() {}
    
    public BoilerpipeArticleParser(XMLReader xmlReader, WebClient webClient) {
        this.xmlReader = xmlReader;
    	this.webClient = webClient;
    }
    
    /**
     * @param webClient Client used to execute web requests
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    /**
     * @param reader Reader used to read HTML content from input stream
     */
    public void setReader(XMLReader reader) {
		this.xmlReader = reader;
	}
    
    @Override
	public List<ArticleFilter> getFilters() {
        return filters;
    }

    @Override
    public void setFilters(List<ArticleFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Article parse(Article article) throws ParserException, IOException {
    	Reader textReader = null;
        try {
        	LOG.info("start parsing article content of {}", article.getTitle());
        	BoilerpipeHTMLContentHandler boilerpipeHandler = new BoilerpipeHTMLContentHandler();
        	
        	WebResponse response = webClient.openWebResponse(new URL(article.getLocation()));
        	textReader = response.getReader();
        	xmlReader.setContentHandler(boilerpipeHandler);
        	InputSource is = new InputSource(textReader);
        	xmlReader.parse(is);
        	TextDocument textDoc = boilerpipeHandler.toTextDocument();
        	LOG.debug("extracting main content from {}", article.getTitle());
            ArticleExtractor.INSTANCE.process(textDoc);
            List<com.cee.news.model.TextBlock> content = article.getContent();
            for (TextBlock block : textDoc.getTextBlocks()) {
                if (block.isContent()) {
                	String[] paragraphs = block.getText().split("\n");
                	for (String paragraph : paragraphs) {
                		content.add(new com.cee.news.model.TextBlock(paragraph, block.getNumWords()));
					}
                }
            }
            if (!accept(article)) {
            	LOG.warn("article with poor content quality found: {}", article.getTitle());
            	return null;
            }
            LOG.info("finished parsing article content of {}, found {} textblocks", article.getTitle(), content.size());
            return article;
        } catch (BoilerpipeProcessingException e) {
            throw new ParserException(e);
        } catch (SAXException e) {
            throw new ParserException(e);
        } finally {
        	if (textReader != null) {
        		textReader.close();
        	}
        }
    }
    
    private boolean accept(Article article) {
        if (filters != null) {
            for (ArticleFilter filter : filters) {
                if (!filter.accept(article)) return false;
            }
        }
        return true;
    }
}
