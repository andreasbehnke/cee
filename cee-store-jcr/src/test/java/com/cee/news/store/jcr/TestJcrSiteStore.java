package com.cee.news.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.cee.news.store.StoreException;
import org.cee.store.test.suite.TestContext;
import org.cee.store.test.suite.TestSiteStore;

public class TestJcrSiteStore extends TestSiteStore {
	
	private final JcrTestContext context;
	
	public TestJcrSiteStore() throws LoginException, RepositoryException, StoreException {
		super();
		context = new JcrTestContext();
	}

	@Override
	protected TestContext getContext() {
		return context;
	}
}
