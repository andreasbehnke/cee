package com.cee.news.store.jcr;

import java.io.File;
import java.io.InputStream;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;
import com.cee.news.store.test.suite.TestContext;

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
	        workingSetStore = new JcrWorkingSetStore(session);
	        siteStore = new JcrSiteStore(session);
	        articleStore = new JcrArticleStore(session);
	        articleSearchService = new JcrArticleSearchService(session);	
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
