package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestContext;
import com.cee.news.store.test.suite.TestSiteStore;

public class TestLuceneSitesStore extends TestSiteStore {

	@Override
	protected TestContext getContext() {
		return new LuceneTestContext();
	}

}
