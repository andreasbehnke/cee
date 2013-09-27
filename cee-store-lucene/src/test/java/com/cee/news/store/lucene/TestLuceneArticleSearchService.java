package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestArticleSearchService;
import com.cee.news.store.test.suite.TestContext;

public class TestLuceneArticleSearchService extends TestArticleSearchService {

	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
