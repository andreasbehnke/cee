package com.cee.news.store;

import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;

/**
 * The article store is responsible for making articles persistent and providing fulltext search.
 */
public interface ArticleStore {
	
    /**
     * If an article with same ID exists, the existing article will be updated.
     * If an article with same ID does not exist, a new article will be created.
     * @param site The articles site
     * @param article The article which needs update
     * @return primary key of the article being added
     */
    ArticleKey update(EntityKey site, Article article) throws StoreException;

	/**
	 * @param listener will be notified about article creation and changes
	 */
	void addArticleChangeListener(ArticleChangeListener listener);

    /**
     * Get article by unique id
     * @param key The key of the article
     * @param withContent If the content should be retrieved
     * @return Article found in repository
     */
    Article getArticle(ArticleKey key, boolean withContent) throws StoreException;
    
    /**
     * Retrieves an article and it's content for the given keys
     * @param keys List of primary keys
     * @param withContent If the content should be retrieved
     * @return List of articles and their content
     */
    List<Article> getArticles(List<ArticleKey> keys, boolean withContent) throws StoreException;
    
    /**
     * Returns a list of unique identifiers of all site's articles
     * @param siteKey The site from which to retrieve identifiers
     * @return List of article identifiers
     */
    List<ArticleKey> getArticlesOrderedByDate(EntityKey siteKey) throws StoreException;
    
    /**
     * Returns a list of unique identifiers of all site's articles
     * @param siteKeys The sites from which to retrieve identifiers
     * @return List of article identifiers
     */
    List<ArticleKey> getArticlesOrderedByDate(List<EntityKey> siteKeys) throws StoreException;
    
    /**
     * Returns a list of unique identifiers of all working set's articles
     * @param workingSet The working set to retrieve all articles from
     * @return List of all article identifiers
     */
    List<ArticleKey> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException;
}
