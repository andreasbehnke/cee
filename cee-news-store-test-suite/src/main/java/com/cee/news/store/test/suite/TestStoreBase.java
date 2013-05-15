package com.cee.news.store.test.suite;

import org.junit.After;
import org.junit.Before;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;

public abstract class TestStoreBase {

	protected abstract TestContext getContext();
	
	protected SiteStore getSiteStore() {
		return getContext().getSiteStore();
	}

	protected ArticleStore getArticleStore() {
		return getContext().getArticleStore();
	}

	protected ArticleSearchService getArticleSearchService() {
		return getContext().getArticleSearchService();
	}

	protected WorkingSetStore getWorkingSetStore() {
		return getContext().getWorkingSetStore();
	}
	
	@Before
	public void open() {
		getContext().open();
	}
	
	@After
	public void close() {
		getContext().close();
	}
}