package org.cee.store.lucene;

import org.cee.store.test.suite.TestContext;
import org.cee.store.test.suite.TestWorkingSetStore;

public class TestLuceneWorkingSetStore extends TestWorkingSetStore {
	
	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}
