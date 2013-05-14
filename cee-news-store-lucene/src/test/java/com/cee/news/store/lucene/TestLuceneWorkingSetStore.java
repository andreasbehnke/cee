package com.cee.news.store.lucene;

import com.cee.news.store.test.suite.TestContext;
import com.cee.news.store.test.suite.TestWorkingSetStore;

public class TestLuceneWorkingSetStore extends TestWorkingSetStore {

	@Override
	protected TestContext getContext() {
		return new LuceneTestContext();
	}

}
