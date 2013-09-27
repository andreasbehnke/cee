package com.cee.news.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import com.cee.news.store.StoreException;
import com.cee.news.store.test.suite.TestContext;
import com.cee.news.store.test.suite.TestWorkingSetStore;

public class TestJcrWorkingSetStore extends TestWorkingSetStore {
    
	private final JcrTestContext context;
	
	public TestJcrWorkingSetStore() throws LoginException, RepositoryException, StoreException {
		super();
		context = new JcrTestContext();
	}
	
	@Override
	protected TestContext getContext() {
		return context;
	}
}
