package com.cee.news.parser.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom.JDOMException;

import com.cee.news.model.Article;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebClient;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

public class RomeFeedParser extends RomeFeedBase implements FeedParser {

    public RomeFeedParser() {
    }

    public RomeFeedParser(WebClient webClient) {
        setWebClient(webClient);
    }

    private SyndFeed readFeed(final URL feedLocation) throws IllegalArgumentException, FeedException, IOException, JDOMException {
        return new SyndFeedInput().build(openDocument(feedLocation));
    }

    @Override
    public List<Article> parse(final URL feedLocation) throws ParserException, IOException {
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
