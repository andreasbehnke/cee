package com.cee.news.store.test.suite;

import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.ArticleChangeListener;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

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
