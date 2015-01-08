package org.cee.parser;

/*
 * #%L
 * Content Extraction Engine - News Parser
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.SiteExtraction;
import org.cee.language.SiteLanguageDetector;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebResponse;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.article.Article;
import org.cee.store.article.ArticleStore;
import org.cee.store.site.Feed;
import org.cee.store.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods for retrieving site's feed data and updating site's articles
 */
public class SiteReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteReader.class);
	
	private FeedParser feedParser;
    
    private SiteParser siteParser;
    
    private SiteLanguageDetector siteLanguageDetector;

    private ArticleStore store;
    
    private ArticleReader articleReader;
    
    public SiteReader() {
    }

    public SiteReader(ArticleStore store, ArticleReader articleReader, FeedParser feedParser, SiteParser siteParser, SiteLanguageDetector siteLanguageDetector) {
        this.store = store;
        this.articleReader = articleReader;
        this.feedParser = feedParser;
        this.siteParser = siteParser;
        this.siteLanguageDetector = siteLanguageDetector;
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
    
    public void setSiteLanguageDetector(SiteLanguageDetector siteLanguageDetector) {
		this.siteLanguageDetector = siteLanguageDetector;
	}

	public void setStore(ArticleStore store) {
        this.store = store;
    }

	private Feed readFeed(WebClient webClient, URL locationUrl) throws MalformedURLException, ParserException, IOException {
    	Reader reader = webClient.openReader(locationUrl);
    	try {
    		return feedParser.parse(reader, locationUrl);
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }

    private List<Feed> readFeeds(WebClient webClient, List<URL> feedLocations) throws IOException, ParserException {
    	List<Feed> feeds = new ArrayList<Feed>();
    	for (URL feedLocation : feedLocations) {
    		feeds.add(readFeed(webClient, feedLocation));
        }
    	return feeds;
    }
    
    private List<Article> processArticles(WebClient webClient, List<Article> articles, EntityKey siteKey, String language) throws StoreException {
    	List<Article> articlesForUpdate = new ArrayList<Article>();
		for (Article article : articles) {
            if (!store.contains(siteKey, article.getExternalId())) {
            	try {
            		article = articleReader.readArticle(webClient, article);
            		if (article != null) {
            			article.setLanguage(language);
            			articlesForUpdate.add(article);
            		}
        		} catch (IOException e) {
    				LOG.warn("Could not retrieve article {}, an io error occured", article.getLocation());
    			} catch (ParserException e) {
    				LOG.warn("Could not parse article {}", article.getLocation());
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
 
    public Feed readFeed(WebClient webClient, String location) throws MalformedURLException, ParserException, IOException {
    	return readFeed(webClient, new URL(location));
    }
    
    public Site readSite(WebClient webClient, String location) throws MalformedURLException, ParserException, IOException {
    	Reader reader = null;
    	try {
    		WebResponse response = webClient.openWebResponse(new URL(location));
    		URL locationUrl = response.getLocation();
        	reader = response.openReaderSource().getReader();
    		SiteExtraction siteExtraction = siteParser.parse(reader, locationUrl);
    		Site site = siteExtraction.getSite();
    		// use site location from response to handle HTTP redirects
    		site.setLocation(locationUrl.toExternalForm());
    		// read all site's feeds
    		site.setFeeds(readFeeds(webClient, siteExtraction.getFeedLocations()));
    		// detect site's language
    		site.setLanguage(siteLanguageDetector.detect(siteExtraction));
    		return site;
    	} finally {
    		IOUtils.closeQuietly(reader);
    	}
    }

    /**
     * Reads the content syndication feed of the site and adds all new articles
     * to the site. The article content is read from the articles web-site.
     */
    public int update(WebClient webClient, Site site) throws StoreException {
        String siteName = site.getName();
    	LOG.info("starting update for site {}", siteName);
        EntityKey siteKey = EntityKey.get(siteName);
        String language = site.getLanguage();
        int siteArticleCount = 0;
    	for (Feed feed : site.getFeeds()) {
    		if (feed.isActive()) {
    			try  {
    				siteArticleCount += processFeed(webClient, feed, siteKey, language);
    			} catch (IOException e) {
    				LOG.warn("Could not retrieve feed {}, an io error occured", feed.getLocation());
    			} catch (ParserException e) {
    				LOG.warn("Could not parse feed {}", feed.getLocation());
				}
            }
        }
    	LOG.info("found {} new articles in site {}", siteArticleCount, siteName);
    	return siteArticleCount;
    }
}
