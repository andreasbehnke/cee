package org.cee.store.lucene;

import org.cee.store.test.suite.TestArticleSearchService;
import org.cee.store.test.suite.TestContext;

public class TestLuceneArticleSearchService extends TestArticleSearchService {

	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
