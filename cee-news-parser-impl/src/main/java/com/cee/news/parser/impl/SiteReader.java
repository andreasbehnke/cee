package com.cee.news.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.SiteExtraction;
import com.cee.news.language.LanguageDetector;
import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteParser;
import com.cee.news.parser.net.WebClient;
import com.cee.news.parser.net.WebResponse;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

/**
 * Provides methods for retrieving site's feed data and updating site's articles
 */
public class SiteReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteReader.class);
	
	private FeedParser feedParser;
    
    private SiteParser siteParser;
    
    private LanguageDetector languageDetector;

    private ArticleStore store;
    
    private ArticleReader articleReader;
    
    public SiteReader() {
    }

    public SiteReader(ArticleStore store, ArticleReader articleReader, FeedParser feedParser, SiteParser siteParser, LanguageDetector languageDetector) {
        this.store = store;
        this.articleReader = articleReader;
        this.feedParser = feedParser;
        this.siteParser = siteParser;
        this.languageDetector = languageDetector;
    }
    
    public void setArticleReader(ArticleReader articleReader) {
	    this.articleReader = articleReader;
    }

    public void setFeedParser(FeedParser feedParser) {
        this.feedParser = feedParser;
    }
    
    public void setSiteParser(SiteParser siteParser) {
	    this.siteParser = siteParser;
    }
    
    public void setLanguageDetector(LanguageDetector languageDetector) {
	    this.languageDetector = languageDetector;
    }

    public void setStore(ArticleStore store) {
        this.store = store;
    }
    
    private List<Feed> readFeeds(WebClient webClient, List<URL> feedLocations) throws IOException, ParserException {
    	List<Feed> feeds = new ArrayList<Feed>();
    	for (URL feedLocation : feedLocations) {
    		Reader feadReader = webClient.openReader(feedLocation);
	        try {
	        	feeds.add(feedParser.parse(feadReader, feedLocation));
	        } finally {
	        	IOUtils.closeQuietly(feadReader);
	        }
        }
    	return feeds;
    }
    
    public Site readSite(WebClient webClient, String location) throws MalformedURLException, ParserException, IOException {
    	URL locationUrl = new URL(location);
    	Reader reader = null;
    	try {
    		WebResponse response = webClient.openWebResponse(locationUrl);
    		reader = response.openReader();
    		SiteExtraction siteExtraction = siteParser.parse(reader, locationUrl);
    		Site site = siteExtraction.getSite();
    		// use site location from response to handle HTTP redirects
    		site.setLocation(response.getLocation().toExternalForm());
    		// read all site's feeds
    		site.setFeeds(readFeeds(webClient, siteExtraction.getFeedLocations()));
    		// detetect site's language
    		site.setLanguage(languageDetector.detect(siteExtraction));
    		return site;
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }
    
    public Feed readFeed(WebClient webClient, String location) throws MalformedURLException, ParserException, IOException {
    	URL locationUrl = new URL(location);
    	Reader reader = webClient.openReader(locationUrl);
    	try {
    		return feedParser.parse(reader, locationUrl);
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }

    /**
     * Reads the content syndication feed of the site and adds all new articles
     * to the site. The article content is read from the articles web-site.
     */
    public int update(WebClient webClient, Site site) throws StoreException, MalformedURLException, ParserException, IOException {
        String siteName = site.getName();
    	LOG.info("starting update for site {}", siteName);
        EntityKey siteKey = EntityKey.get(siteName);
        String language = site.getLanguage();
        int siteArticleCount = 0;
    	for (Feed feed : site.getFeeds()) {
    		if (feed.isActive()) {
    			siteArticleCount += processFeed(webClient, feed, siteKey, language);
            }
        }
    	if (LOG.isInfoEnabled() && siteArticleCount > 0) {
    		LOG.info("found {} new articles in site {}", siteArticleCount, siteName);
    	}
        return siteArticleCount;
    }
 
    private List<Article> processArticles(WebClient webClient, List<Article> articles, EntityKey siteKey, String language) throws MalformedURLException, StoreException, IOException {
    	List<Article> articlesForUpdate = new ArrayList<Article>();
		for (Article article : articles) {
            if (!store.contains(siteKey, article.getExternalId())) {
            	article = articleReader.readArticle(webClient, article);
            	if (article != null) {
        			article.setLanguage(language);
        			articlesForUpdate.add(article);
        		}
            }
        }
		return articlesForUpdate;
    }
 
    private int processFeed(WebClient webClient, Feed feed, EntityKey siteKey, String language) throws MalformedURLException, ParserException, IOException, StoreException {
    	LOG.debug("processing feed {}", feed.getTitle());
    	URL location = new URL(feed.getLocation());
    	Reader reader = webClient.openReader(location);
    	try {
    		int articleCount = 0;
			List<Article> articles = feedParser.readArticles(reader, location);
			List<Article> articlesForUpdate = processArticles(webClient, articles, siteKey, language);
			if (articlesForUpdate.size() > 0) {
				articleCount = store.addNewArticles(siteKey, articlesForUpdate).size();
				LOG.debug("found {} new articles in feed {}", articleCount, feed.getTitle());
			}
			return articleCount;
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }
}
