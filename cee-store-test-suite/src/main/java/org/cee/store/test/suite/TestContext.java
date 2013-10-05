package org.cee.store.test.suite;

import org.cee.news.store.ArticleStore;
import org.cee.news.store.SiteStore;
import org.cee.news.store.WorkingSetStore;
import org.cee.search.ArticleSearchService;

public interface TestContext {

	public abstract void open();
	
	public abstract void close();
	
	public abstract SiteStore getSiteStore();

	public abstract ArticleStore getArticleStore();

	public abstract ArticleSearchService getArticleSearchService();

	public abstract WorkingSetStore getWorkingSetStore();

}