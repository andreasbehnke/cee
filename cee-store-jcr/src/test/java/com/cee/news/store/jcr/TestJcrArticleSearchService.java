package com.cee.news.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import com.cee.news.store.StoreException;
import com.cee.news.store.test.suite.TestArticleSearchService;
import com.cee.news.store.test.suite.TestContext;

public class TestJcrArticleSearchService extends TestArticleSearchService {

	private final JcrTestContext context;
	
	public TestJcrArticleSearchService() throws LoginException, RepositoryException, StoreException {
		super();
		context = new JcrTestContext();
	}

	@Override
	protected TestContext getContext() {
		return context;
	}
}