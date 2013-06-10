package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestContext;
import com.cee.news.store.test.suite.TestWorkingSetStore;

public class TestLuceneWorkingSetStore extends TestWorkingSetStore {
	
	private TestContext context = new LuceneTestContext();

	@Override
	protected TestContext getContext() {
		return context;
	}

}