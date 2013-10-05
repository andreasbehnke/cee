package org.cee.store.lucene;

import org.cee.store.test.suite.TestContext;
import org.cee.store.test.suite.TestSiteStore;

public class TestLuceneSitesStore extends TestSiteStore {

	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
