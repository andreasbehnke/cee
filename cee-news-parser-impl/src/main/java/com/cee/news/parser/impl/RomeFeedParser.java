package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filter;
import org.xml.sax.InputSource;

import com.cee.news.model.Article;
import com.cee.news.model.Feed;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
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

public class RomeFeedParser extends WireFeedInput implements FeedParser {
	
	private static FeedParsers feedParsers =  new FeedParsers();

    public RomeFeedParser() {
    }

    public RomeFeedParser(WebClient webClient) {
        setWebClient(webClient);
    }
    
	@SuppressWarnings("serial")
	private class EmptyElementFilter implements Filter {

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof Element) {
				Element element = (Element)obj;
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
	
	private WebClient webClient;
	
    /**
     * @param webClient Client used to execute web requests
     */
	public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
	}
	
	private void removeEmptyElements(Element element) {
		element.removeContent(new EmptyElementFilter());
		for (Object	obj : element.getContent()) {
			if (obj instanceof Element) {
				removeEmptyElements((Element)obj);
			}
		}
	}
	
	private Document openDocument(final URL feedLocation) throws JDOMException, IOException {
		SAXBuilder saxBuilder = createSAXBuilder();
		WebResponse response = webClient.openWebResponse(feedLocation);
	    Document document = null;
	    Reader reader = null;
	    try {
	    	reader = new XmlFixerReader(response.getReader());
	    	document = saxBuilder.build(new InputSource(reader));
	    } finally {
	    	if (reader != null)
	    		reader.close();
	    }
	    removeEmptyElements(document.getRootElement());
	    return document;
	}
    
    private SyndFeed readFeed(final URL feedLocation) throws IllegalArgumentException, FeedException, IOException, JDOMException {
        return new SyndFeedInput().build(openDocument(feedLocation));
    }

    @Override
    public boolean isSupportedFeed(final URL feedLocation) throws IOException {
        Document document = null;
		try {
			document = openDocument(feedLocation);
		} catch (JDOMException e) {
			return false;
		}
        WireFeedParser parser = feedParsers.getParserFor(document);
        return parser != null;
    }
    
    @Override
    public Feed parse(URL feedLocation) throws IOException {
    	try {
    		Document document = openDocument(feedLocation);
    		SyndFeedInput input = new SyndFeedInput();
    		SyndFeed syndFeed = input.build(document);
    		Feed feed = new Feed(feedLocation.toExternalForm(), syndFeed.getTitle());
    		feed.setLanguage(syndFeed.getLanguage());
    		return feed;
		} catch (JDOMException | IllegalArgumentException | FeedException e) {
			throw new IOException(e);
		}
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<Article> readArticles(final URL feedLocation) throws ParserException, IOException {
        List<Article> articles = new ArrayList<Article>();

        SyndFeed syndFeed = null;
        try {
            syndFeed = readFeed(feedLocation);
        } catch (IllegalArgumentException e) {
            throw new ParserException("Unknown type of feed.", e);
        } catch (FeedException e) {
            throw new ParserException("Could not parse feed.", e);
        } catch (JDOMException e) {
        	throw new ParserException("Could not parse feed.", e);
		}

        for (SyndEntry entry : (List<SyndEntry>) syndFeed.getEntries()) {
            Article article = new Article();
            article.setTitle(entry.getTitle());
            SyndContent syndContent = entry.getDescription();
            if (syndContent != null) {
                article.setShortText(syndContent.getValue());
            }
            String link = entry.getLink();
            String id = entry.getUri();
            if (id == null) {
                id = link;
            }
            article.setExternalId(id);
            try {
            	URL articleUrl = new URL(feedLocation, link);//check for well formed URL
                article.setLocation(articleUrl.toExternalForm());
            } catch (MalformedURLException e) {
                throw new ParserException("The article has an invalid URL.", e);
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
