package com.cee.news.store.lucene;

import java.util.List;

import com.cee.news.model.Article;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.ArticleChangeListener;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

public class LuceneArticleStore extends LuceneStoreBase implements ArticleStore {

	@Override
	public ArticleKey update(EntityKey site, Article article) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addArticleChangeListener(ArticleChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Article getArticle(ArticleKey key, boolean withContent) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Article> getArticles(List<ArticleKey> keys, boolean withContent) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(EntityKey siteKey) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(List<EntityKey> siteKeys) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleKey> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
