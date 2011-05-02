package com.cee.news.parser;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

/**
 * SiteUpdater searches for new articles and adds them to the site
 */
public class SiteUpdater {

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
        int articleCount = 0;
        for (Feed feed : site.getFeeds()) {
            if (onlyActiveFeeds && !feed.isActive()) {
                continue;
            }
            List<Article> articles = null;
            articles = feedParser.parse(new URL(feed.getLocation()));
            for (Article article : articles) {
                store.update(site, articleParser.parse(article));
                articleCount++;
                // TODO: Implement equality compare and add new article version
                // if necessary
            }
        }
        return articleCount;
    }
}
