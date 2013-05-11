package com.cee.news.store.test.suite;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;

public interface TestContext {

	public abstract void open();
	
	public abstract void close();
	
	public abstract SiteStore getSiteStore();

	public abstract ArticleStore getArticleStore();

	public abstract ArticleSearchService getArticleSearchService();

	public abstract WorkingSetStore getWorkingSetStore();

}