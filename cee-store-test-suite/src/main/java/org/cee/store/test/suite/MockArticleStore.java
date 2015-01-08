package org.cee.store.test.suite;

/*
 * #%L
 * Content Extraction Engine - News Store Test Suite
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
import org.cee.store.article.Article;
import org.cee.store.article.ArticleChangeListener;
import org.cee.store.article.ArticleKey;
import org.cee.store.article.ArticleStore;
import org.cee.store.workingset.WorkingSet;

public class MockArticleStore implements ArticleStore {

	@Override
    public ArticleKey update(EntityKey site, Article article) throws StoreException {
	    return null;
    }

	@Override
    public boolean contains(EntityKey site, String externalId) throws StoreException {
	    return false;
    }

	@Override
    public List<ArticleKey> addNewArticles(EntityKey site, List<Article> articles) throws StoreException {
	    return null;
    }

	@Override
    public void addArticleChangeListener(ArticleChangeListener listener) {
	    
    }

	@Override
    public Article getArticle(ArticleKey key, boolean withContent) throws StoreException {
	    return null;
    }

	@Override
    public List<Article> getArticles(List<ArticleKey> keys, boolean withContent) throws StoreException {
	    return null;
    }

	@Override
    public List<ArticleKey> getArticlesOrderedByDate(EntityKey siteKey) throws StoreException {
	    return null;
    }

	@Override
    public List<ArticleKey> getArticlesOrderedByDate(List<EntityKey> siteKeys) throws StoreException {
	    return null;
    }

	@Override
    public List<ArticleKey> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException {
	    return null;
    }

}
