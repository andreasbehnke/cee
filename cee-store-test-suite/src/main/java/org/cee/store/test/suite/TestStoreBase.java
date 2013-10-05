package org.cee.store.test.suite;

import org.cee.news.store.ArticleStore;
import org.cee.news.store.SiteStore;
import org.cee.news.store.WorkingSetStore;
import org.cee.search.ArticleSearchService;
import org.junit.After;
import org.junit.Before;

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