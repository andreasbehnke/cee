package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.xml.sax.InputSource;

import com.cee.news.parser.FeedChecker;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.sun.syndication.io.SAXBuilder;
import com.sun.syndication.io.WireFeedInput;
import com.sun.syndication.io.WireFeedParser;
import com.sun.syndication.io.impl.FeedParsers;

public class RomeFeedChecker extends WireFeedInput implements FeedChecker {
    
    private static FeedParsers feedParsers;
    
    private WebClient webClient;
    
    public RomeFeedChecker() {
        synchronized (RomeFeedChecker.class) {
            if (feedParsers == null) {
                feedParsers = new FeedParsers();
            }
        }
    }
    
    public RomeFeedChecker(WebClient webClient) {
        this();
        this.webClient = webClient;
    }
    
    /**
     * @param webClient Client used to execute web requests
     */
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean isSupportedFeed(final URL feedLocation) throws IOException {
        SAXBuilder saxBuilder = createSAXBuilder();
        
        WebResponse response = webClient.openWebResponse(feedLocation);
        Document document = null;
        Reader reader = null;
        try {
        	reader = response.getReader();
        	document = saxBuilder.build(new InputSource(reader));
        } catch (JDOMException e) {
            throw new IOException("Could not parse feed stream", e);
        } finally {
        	if (reader != null)
        		reader.close();
        }
        WireFeedParser parser = feedParsers.getParserFor(document);
        return parser != null;
    }
}
