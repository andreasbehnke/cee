package org.cee.store.lucene;

import org.cee.store.test.suite.TestArticleStore;
import org.cee.store.test.suite.TestContext;

public class TestLuceneArticleStore extends TestArticleStore {

	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
