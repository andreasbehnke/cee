package org.cee.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.cee.store.StoreException;
import org.cee.store.test.suite.TestContext;
import org.cee.store.test.suite.TestWorkingSetStore;

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
