package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestArticleStore;
import com.cee.news.store.test.suite.TestContext;

public class TestLuceneArticleStore extends TestArticleStore {

	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
