package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestArticleStore;
import com.cee.news.store.test.suite.TestContext;

public class TestLuceneArticleStore extends TestArticleStore {

	@Override
	protected TestContext getContext() {
		return new LuceneTestContext();
	}

}
