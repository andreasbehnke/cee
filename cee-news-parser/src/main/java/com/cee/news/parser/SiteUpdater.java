package com.cee.news.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
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
     *             Thrown if a site's feed could not be parsed
     * @throws StoreException
     *             Thrown if an article could not be stored
     * @throws MalformedURLException 
     * @throws IOException
     *             If an IO error occurred while retrieving a feed
     */
    public int update(Site site) throws StoreException, MalformedURLException, ParserException, IOException {
        LOG.info("starting update for site {}", site.getName());
    	List<Article> articles = new ArrayList<Article>();
        for (Feed feed : site.getFeeds()) {
            if (!onlyActiveFeeds || feed.isActive()) {
            	LOG.debug("processing feed {}", feed.getTitle());
                articles.addAll(feedParser.readArticles(new URL(feed.getLocation())));
            }
        }
        int articleCount = updateArticles(EntityKey.get(site.getName()), articles);
        LOG.info("updated {} articles of site {}", articleCount, site.getName());
        return articleCount;
    }
    
    /**
     * Updates all articles of a site
     * @param site
     * @param feed
     * @return number of articles processed
     * @throws ParserException If the feed could not be parsed
     * @throws IOException If the feed could not be read
     * @throws StoreException If the storage of articles failed
     */
    protected int updateArticles(EntityKey siteKey, List<Article> articles) throws StoreException {
    	List<Article> articlesForUpdate = new ArrayList<Article>();
        for (Article article : articles) {
        	try {
            	article = articleParser.parse(article);
            	if (article != null) {
            		articlesForUpdate.add(article);
            	}
        	} catch(ParserException e) {
        		LOG.error("could not parse article {}", article.getTitle(), e);
        	} catch (IOException e) {
        		LOG.error("could retrieve article {}", article.getTitle(), e);
			}
        }
        return store.addNewArticles(siteKey, articlesForUpdate).size();
    }
}
