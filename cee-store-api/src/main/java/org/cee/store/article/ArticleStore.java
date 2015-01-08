package org.cee.store.article;

/*
 * #%L
 * Content Extraction Engine - News Store API
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


import java.util.List;

import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.workingset.WorkingSet;

/**
 * The article store is responsible for making articles persistent and providing fulltext search.
 */
public interface ArticleStore {
	
    /**
     * If an article with same ID exists, the existing article will be updated.
     * If an article with same ID does not exist, a new article will be created.
     * @param site The article's site
     * @param article The article which needs update
     * @return primary key of the article being added
     */
    ArticleKey update(EntityKey site, Article article) throws StoreException;
    
    /**
     * Tests existence of article 
     * @param site The articles site
     * @param externalId External article id
     * @return true if article with externalId exists within this store
     */
    boolean contains(EntityKey site, String externalId) throws StoreException;
    
    /**
     * Adds all articles, which do not exist in store
     * @param site The article's site
     * @param articles List of articles to add. Only new articles will be added.
     * @return List of primary keys which have been added
     */
    List<ArticleKey> addNewArticles(EntityKey site, List<Article> articles) throws StoreException;

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
