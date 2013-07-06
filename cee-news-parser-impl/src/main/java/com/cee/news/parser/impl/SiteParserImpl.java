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

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;

/**
 * Extracts feed URLs from the header of a HTML document.
 * Sets the title and the description of the created site using
 * the meta information from document header. Detects site language
 * using {@link LanguageDetector}.
 */
public class SiteParserImpl implements SiteParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteParserImpl.class);

    private WebClient webClient;
    
    private XMLReader xmlReader;
    
    private FeedParser feedParser;
    
    private LanguageDetector languageDetector;

    /**
     * {@link XMLReader} used by the parser.
     * @param reader
     *            The XMLReader instance used to parse HTML content
     */
    public void setReader(XMLReader reader) {
        this.xmlReader = reader;
    }
    
    /**
     * @param feedParser {@link FeedParser} instance used to test, if an URL points to a supported feed type
     */
    public void setFeedParser(FeedParser feedParser) {
        this.feedParser = feedParser;
    }
    
    /**
     * @param webClient The web client used for web connections
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public void setLanguageDetector(LanguageDetector languageDetector) {
	    this.languageDetector = languageDetector;
    }

    public SiteParserImpl() {}
    
    public SiteParserImpl(XMLReader xmlReader, FeedParser feedParser, LanguageDetector languageDetector, WebClient webClient) {
        this.xmlReader = xmlReader;
        this.feedParser = feedParser;
        this.webClient = webClient;
        this.languageDetector =  languageDetector;
    }

    /**
     * Parses the HTML content and extracts all feeds using the provided reader.
     * The feed instances are created by the feedService for each LINK element
     * with a rel attribute set to "alternate".
     * 
     * @param siteLocation
     *            The URL of the content to be parsed
     * @return The site parsed from the given location
     * @throws IOException
     *             If the stream could not be opened
     * @throws SAXException
     *             If the input source could not be parsed
     */
    @Override
    public SiteExtraction parse(URL siteLocation) throws IOException, ParserException {
        if (xmlReader == null) {
            throw new IllegalStateException("reader property has not been set");
        }
        if (feedParser == null) {
            throw new IllegalStateException("feedChecker property has not been set");
        }

        WebResponse webResponse = webClient.openWebResponse(siteLocation);
        
        SiteHandler siteHandler = new SiteHandler(webResponse.getLocation());
        xmlReader.setContentHandler(siteHandler);
        Reader reader = null;
        try {
        	reader = webResponse.getReader();
        	InputSource is = new InputSource(reader);
        	LOG.info("start parsing site document {}", siteLocation);
            xmlReader.parse(is);
        } catch (SAXException e) { 
        	throw new ParserException("Could not parse site", e);
        } finally {
        	LOG.info("finished parsing site document {}", siteLocation);
        	if (reader != null) {
        		reader.close();
        	}
        }
        
        SiteExtraction siteExtraction = siteHandler.getSiteExtraction();
        Site site = siteExtraction.getSite();
        //parse all feeds found in site
        List<URL> feedLocations = siteExtraction.getFeedLocations();
        List<Feed> feeds = new ArrayList<Feed>();
        for (URL feedLocation : feedLocations) {
        	if (!feedParser.isSupportedFeed(feedLocation)) {
            	if (LOG.isDebugEnabled()) {
            		LOG.debug("unknown feed type found: {}", feedLocation);
            	}
            } else {
            	feeds.add(feedParser.parse(feedLocation));
            }
        }
        site.setFeeds(feeds);
        String language = languageDetector.detect(siteExtraction);
        site.setLanguage(language);
        return siteExtraction;
    }
}
