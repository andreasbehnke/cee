package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Article;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

/**
 * SiteUpdater searches for new articles and adds them to the site
 */
public class SiteUpdater {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteUpdater.class);

    private ArticleParser articleParser;

    private FeedParser feedParser;

    private ArticleStore store;

    private boolean onlyActiveFeeds = true;

    public SiteUpdater() {
    }

    public SiteUpdater(ArticleStore store, ArticleParser articleParser, FeedParser feedParser) {
        this.store = store;
        this.articleParser = articleParser;
        this.feedParser = feedParser;
    }

    public SiteUpdater(ArticleStore store, ArticleParser articleParser, FeedParser feedParser, boolean onlyActiveFeeds) {
        this.store = store;
        this.articleParser = articleParser;
        this.feedParser = feedParser;
        this.onlyActiveFeeds = onlyActiveFeeds;
    }

    /**
     * @param articleParser
     *            Parser used to read the content of the articles web-site
     */
    public void setArticleParser(ArticleParser articleParser) {
        this.articleParser = articleParser;
    }

    /**
     * @param feedParser
     *            Parser used to read the content syndication feed of the site
     */
    public void setFeedParser(FeedParser feedParser) {
        this.feedParser = feedParser;
    }

    /**
     * @param store
     *            The store used to update the articles
     */
    public void setStore(ArticleStore store) {
        this.store = store;
    }

    /**
     * @param onlyActiveFeeds
     *            If true, only active feeds are parsed. Default is true.
     */
    public void setOnlyActiveFeeds(boolean onlyActiveFeeds) {
        this.onlyActiveFeeds = onlyActiveFeeds;
    }

    /**
     * Reads the content syndication feed of the site and adds all new articles
     * to the site. The article content is read from the articles web-site.
     * 
     * @param site
     *            The site to be updated
     * @return Number of added articles
     * @throws ParserException
     *             Thrown if the feed or the article could not be parsed
     * @throws StoreException
     *             Thrown if the article could not be stored
     * @throws IOException
     *             If an IO error occurred
     */
    public int update(Site site) throws ParserException, StoreException, IOException {
        LOG.info("starting update for site {}", site.getName());
    	int articleCount = 0;
        for (Feed feed : site.getFeeds()) {
            if (onlyActiveFeeds && !feed.isActive()) {
                continue;
            }
            LOG.debug("processing feed {}", feed.getTitle());
            List<Article> articles = null;
            articles = feedParser.parse(new URL(feed.getLocation()));
            for (Article article : articles) {
            	try {
            		article = articleParser.parse(article);
            		try {
                		store.update(site, article);
                		articleCount++;
                	} catch (Exception e) {
                		LOG.error("could not store article {}: {}", article.getTitle(), e);
    				}
            	} catch(Exception e) {
            		LOG.error("could not parse article {}: {}", article.getTitle(), e);
            	}
                // TODO: Implement equality compare and add new article version
                // if necessary
            }
        }
        LOG.info("updated {} articles of site {}", articleCount, site.getName());
        return articleCount;
    }
}
