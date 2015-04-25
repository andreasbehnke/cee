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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.cee.SiteExtraction;
import org.cee.language.SiteLanguageDetector;
import org.cee.net.WebClient;
import org.cee.net.WebResponse;
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
	
	private final ForkJoinPool pool = new ForkJoinPool();
	
	private WebClient webClient;
	
	private FeedParser feedParser;
    
    private SiteParser siteParser;
    
    private SiteLanguageDetector siteLanguageDetector;

    private ArticleStore store;
    
    private ArticleReader articleReader;

    public SiteReader() {
    }

    public SiteReader(WebClient webClient, ArticleStore store, ArticleReader articleReader, FeedParser feedParser, SiteParser siteParser, SiteLanguageDetector siteLanguageDetector) {
        this.webClient = webClient;
    	this.store = store;
        this.articleReader = articleReader;
        this.feedParser = feedParser;
        this.siteParser = siteParser;
        this.siteLanguageDetector = siteLanguageDetector;
    }
    
    public void setWebClient(WebClient webClient) {
		this.webClient = webClient;
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

	private Feed readFeed(URL locationUrl) throws MalformedURLException, ParserException, IOException {
    	try (Reader reader = this.webClient.openWebResponse(locationUrl, false).openReader()) {
    		return feedParser.parse(reader, locationUrl);
    	}
    }

    private List<Feed> readFeeds(List<URL> feedLocations) throws IOException, ParserException {
    	List<Feed> feeds = new ArrayList<Feed>();
    	for (URL feedLocation : feedLocations) {
    		feeds.add(readFeed(feedLocation));
        }
    	return feeds;
    }
    
    private List<Article> processArticles(final List<Article> articles, final EntityKey siteKey, final String language) throws StoreException, InterruptedException, ExecutionException {
    	List<Callable<Article>> tasks = new ArrayList<Callable<Article>>();
		for (final Article article : articles) {
            tasks.add(new Callable<Article>() {
		        @Override
		        public Article call() throws Exception {
		            Article result = null;
		            try {
		                if (!store.contains(siteKey, article.getExternalId())) {
		                    result = articleReader.readArticle(article);
		                    if (result != null) {
		                        result.setLanguage(language);
		                    }
		                }
		            } catch (MalformedURLException e) {
		                LOG.warn("Article has malformed URL: {}, ", article.getLocation());
		            } catch (IOException e) {
		                LOG.warn("Could not retrieve article {}, an io error occured", article.getLocation());
		                LOG.warn("IO error:", e);
		            } catch (ParserException e) {
		                LOG.warn("Could not parse article {}", article.getLocation());
		                LOG.warn("Parser error:", e);
		            }
		            return result;
		        }
            });
        }
		List<Future<Article>> results = pool.invokeAll(tasks);
		List<Article> articlesForUpdate = new ArrayList<Article>();
        for (Future<Article> future : results) {
            Article article = future.get();
            if (article != null) {
                articlesForUpdate.add(article);
            }
        }
		return articlesForUpdate;
    }
 
    private int processFeed(Feed feed, EntityKey siteKey, String language) throws MalformedURLException, ParserException, IOException, StoreException, InterruptedException, ExecutionException {
    	LOG.debug("processing feed {}", feed.getTitle());
    	URL location = new URL(feed.getLocation());
    	try (Reader reader = this.webClient.openWebResponse(location, false).openReader()) {
    		int articleCount = 0;
			List<Article> articles = feedParser.readArticles(reader, location);
			List<Article> articlesForUpdate = processArticles(articles, siteKey, language);
			if (articlesForUpdate.size() > 0) {
				articleCount = store.addNewArticles(siteKey, articlesForUpdate).size();
				LOG.debug("found {} new articles in feed {}", articleCount, feed.getTitle());
			}
			return articleCount;
    	}
    }
 
    public Feed readFeed(String location) throws MalformedURLException, ParserException, IOException {
    	return readFeed(new URL(location));
    }
    
    public Site readSite(String location) throws MalformedURLException, ParserException, IOException {
    	WebResponse response = this.webClient.openWebResponse(new URL(location), false);
        URL locationUrl = response.getLocation();
        try (Reader reader = response.openReader()) {
    		SiteExtraction siteExtraction = siteParser.parse(reader, locationUrl);
    		Site site = siteExtraction.getSite();
    		// use site location from response to handle HTTP redirects
    		site.setLocation(locationUrl.toExternalForm());
    		// read all site's feeds
    		site.setFeeds(readFeeds(siteExtraction.getFeedLocations()));
    		// detect site's language
    		site.setLanguage(siteLanguageDetector.detect(siteExtraction));
    		return site;
    	}
    }

    /**
     * Reads the content syndication feed of the site and adds all new articles
     * to the site. The article content is read from the articles web-site.
     */
    public int update(Site site) throws StoreException {
        String siteName = site.getName();
    	LOG.info("starting update for site {}", siteName);
        EntityKey siteKey = EntityKey.get(siteName);
        String language = site.getLanguage();
        int siteArticleCount = 0;
    	for (Feed feed : site.getFeeds()) {
    		if (feed.isActive()) {
    			try  {
    				siteArticleCount += processFeed(feed, siteKey, language);
    			} catch (IOException e) {
    				LOG.warn("Could not retrieve feed {}, an io error occured", feed.getLocation());
    			} catch (ParserException e) {
    				LOG.warn("Could not parse feed {}", feed.getLocation());
				} catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    	LOG.info("found {} new articles in site {}", siteArticleCount, siteName);
    	return siteArticleCount;
    }
}
