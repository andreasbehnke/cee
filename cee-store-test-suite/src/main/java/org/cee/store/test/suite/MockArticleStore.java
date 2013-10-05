package org.cee.store.test.suite;

import java.util.List;

import org.cee.news.model.Article;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.ArticleChangeListener;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.StoreException;

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
