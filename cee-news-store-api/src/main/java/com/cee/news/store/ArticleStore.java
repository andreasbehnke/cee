package com.cee.news.store;

import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;

/**
 * The article store is responsible for making articles persistent and providing fulltext search.
 */
public interface ArticleStore {

    /**
     * If an article with same ID exists, the existing article will be updated. TODO: Provide version functionality!
     * If an article with same ID does not exist, a new article will be created.
     * @param site The articles site
     * @param article The article which needs update
     */
    void update(Site site, Article article) throws StoreException;
    
    /**
     * Get article by unique id
     * @param location The location of the article
     * @return Article found in repository
     */
    Article getArticle(String location) throws StoreException;
    
    /**
     * Returns a list of unique identifiers of all site's articles
     * @param site The site from which to retrieve identifiers
     * @return List of article identifiers
     */
    List<String> getArticlesOrderedByDate(Site site) throws StoreException;
    
    /**
     * Returns the contents of the article with given location
     * @param location Identifier of article
     * @return The text blocks of this article
     */
    List<TextBlock> getContent(String location) throws StoreException;
    
}
