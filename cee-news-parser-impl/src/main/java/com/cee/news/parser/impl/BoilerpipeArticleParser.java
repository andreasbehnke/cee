package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
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
public class BoilerpipeArticleParser implements ArticleParser {
	
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
        	String articleTitel = article.getTitle();
        	LOG.debug("start parsing article content of {}", article.getLocation());
        	BoilerpipeHTMLContentHandler boilerpipeHandler = new BoilerpipeHTMLContentHandler();
        	
        	WebResponse response = webClient.openWebResponse(new URL(article.getLocation()));
        	textReader = response.getReader();
        	xmlReader.setContentHandler(boilerpipeHandler);
        	InputSource is = new InputSource(textReader);
        	xmlReader.parse(is);
        	TextDocument textDoc = boilerpipeHandler.toTextDocument();
        	textDoc.setTitle(articleTitel);
        	String htmlTitle = boilerpipeHandler.getTitle();
        	
        	LOG.debug("extracting main content from {}", article.getLocation());
        	if (!new ArticleExtractor(htmlTitle).process(textDoc)) {
        		LOG.warn("Parsing of article {} failed", article.getLocation());
        		return null;
        	}
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
            	LOG.warn("article with poor content quality found: {}", article.getLocation());
            	return null;
            }
            LOG.debug("finished parsing article content of {}, found {} textblocks", article.getLocation(), content.size());
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
