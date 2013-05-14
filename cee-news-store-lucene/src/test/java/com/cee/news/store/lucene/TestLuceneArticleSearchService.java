package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestArticleSearchService;
import com.cee.news.store.test.suite.TestContext;

public class TestLuceneArticleSearchService extends TestArticleSearchService {

	@Override
	protected TestContext getContext() {
		return new LuceneTestContext();
	}

}
