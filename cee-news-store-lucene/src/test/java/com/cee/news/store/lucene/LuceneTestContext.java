package com.cee.news.store.lucene;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;
import com.cee.news.store.test.suite.TestContext;

public class LuceneTestContext implements TestContext {

	SiteStore siteStore = new LuceneSiteStore();

	ArticleStore articleStore = new LuceneArticleStore();
	
	ArticleSearchService articleSearchService = new LuceneArticleSearchService();

	WorkingSetStore workingSetStore = new LuceneWorkingSetStore();
	
	@Override
	public void open() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public SiteStore getSiteStore() {
		return siteStore;
	}

	@Override
	public ArticleStore getArticleStore() {
		return articleStore;
	}

	@Override
	public ArticleSearchService getArticleSearchService() {
		return articleSearchService;
	}

	@Override
	public WorkingSetStore getWorkingSetStore() {
		return workingSetStore;
	}

}
