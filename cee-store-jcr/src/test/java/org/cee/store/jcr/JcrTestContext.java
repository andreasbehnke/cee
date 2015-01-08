package org.cee.store.jcr;

import java.io.File;
import java.io.InputStream;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.cee.search.ArticleSearchService;
import org.cee.store.article.ArticleStore;
import org.cee.store.jcr.JcrArticleSearchService;
import org.cee.store.jcr.JcrArticleStore;
import org.cee.store.jcr.JcrSiteStore;
import org.cee.store.jcr.JcrStoreInitializer;
import org.cee.store.jcr.JcrWorkingSetStore;
import org.cee.store.jcr.SessionManager;
import org.cee.store.site.SiteStore;
import org.cee.store.test.suite.TestContext;
import org.cee.store.workingset.WorkingSetStore;

public class JcrTestContext implements TestContext {

    private final static String TEST_REPOSITORY_DIR = "repository";

    private Repository repository;

    private Session session;

    private JcrSiteStore siteStore;

    private JcrArticleStore articleStore;

    private JcrArticleSearchService articleSearchService;

    private JcrWorkingSetStore workingSetStore;
    
    @Override
    public void open() {
    	try {
	    	deleteTestRepository();
	        InputStream configInput = JcrTestContext.class.getResourceAsStream("repository.xml");
	        RepositoryConfig config = RepositoryConfig.create(configInput, TEST_REPOSITORY_DIR);
	        repository = new TransientRepository(config);
	        session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
	        JcrStoreInitializer init = new JcrStoreInitializer();
	        init.setSession(session);
	        init.registerNodeTypes();
	        SessionManager sessionManager = new SessionManager() {
				@Override
				public Session getSession() {
					return session;
				}
			};
	        workingSetStore = new JcrWorkingSetStore(sessionManager);
	        siteStore = new JcrSiteStore(sessionManager);
	        articleStore = new JcrArticleStore(sessionManager);
	        articleSearchService = new JcrArticleSearchService(sessionManager);	
    	} catch(Exception e) {
    		throw new RuntimeException("Could not open JCR context", e);
    	}
    }
    
    @Override
    public void close() {
    	if (session != null) {
            session.logout();
        }
        deleteTestRepository();
    }
    
    @Override
	public SiteStore getSiteStore() {
		return siteStore;
	}

	@Override
	public ArticleStore getArticleStore() {
		return articleStore;
	}

	@Override
	public ArticleSearchService getArticleSearchService() {
		return articleSearchService;
	}

	@Override
	public WorkingSetStore getWorkingSetStore() {
		return workingSetStore;
	}

	public void setWorkingSetStore(JcrWorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}

	private void deleteFile(File file) {
        if (file.isDirectory()) {
            for (String child : file.list()) {
                deleteFile(new File(file, child));
            }
        }
        if (!file.delete()) {
            throw new RuntimeException("Could not delete repository!");
        }
    }

    private void deleteTestRepository() {
        File testDir = new File(TEST_REPOSITORY_DIR);
        if (testDir.exists()) {
            deleteFile(testDir);
        }
    }
}
