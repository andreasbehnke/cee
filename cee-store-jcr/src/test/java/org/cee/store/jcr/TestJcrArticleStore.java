package org.cee.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.cee.store.StoreException;
import org.cee.store.test.suite.TestArticleStore;
import org.cee.store.test.suite.TestContext;

public class TestJcrArticleStore extends TestArticleStore {

	private final JcrTestContext context;
	
	public TestJcrArticleStore() throws LoginException, RepositoryException, StoreException {
		super();
		context = new JcrTestContext();
	}

	@Override
	protected TestContext getContext() {
		return context;
	}
}
