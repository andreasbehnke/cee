package org.cee.store.jcr;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.cee.store.StoreException;
import org.cee.store.test.suite.TestArticleSearchService;
import org.cee.store.test.suite.TestContext;

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