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
	    public static final ArticleExtractor INSTANCE = new ArticleExtractor();
	    
	    private final BoilerpipeFilter terminatingBlocksFinder;
	    
	    private ArticleExtractor() {
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

	    public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
	    	BoilerpipeFilter titleFinder = new DocumentTitleMatchFilter(
	    			DefaultLabels.TITLE, 
	    			doc.getTitle(),
	    			0.1, 
	    			false);
	    	
	    	boolean changed = terminatingBlocksFinder.process(doc);
	    	boolean foundTitle = titleFinder.process(doc);
	    	if (!foundTitle) {
	    		LOG.warn("Article title not found in content of article {}", doc.getTitle());
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
        	LOG.debug("start parsing article content of {}", articleTitel);
        	BoilerpipeHTMLContentHandler boilerpipeHandler = new BoilerpipeHTMLContentHandler();
        	
        	WebResponse response = webClient.openWebResponse(new URL(article.getLocation()));
        	textReader = response.getReader();
        	xmlReader.setContentHandler(boilerpipeHandler);
        	InputSource is = new InputSource(textReader);
        	xmlReader.parse(is);
        	TextDocument textDoc = boilerpipeHandler.toTextDocument();
        	LOG.debug("extracting main content from {}", articleTitel);
        	if (articleTitel != null) {
        		textDoc.setTitle(articleTitel);
            }

        	if (!ArticleExtractor.INSTANCE.process(textDoc)) {
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
            	LOG.warn("article with poor content quality found: {}", article.getTitle());
            	return null;
            }
            LOG.debug("finished parsing article content of {}, found {} textblocks", article.getTitle(), content.size());
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
