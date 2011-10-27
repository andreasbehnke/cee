package com.cee.news.store.jcr;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.store.StoreException;

public abstract class JcrTestBase {
    
    private final static String TEST_REPOSITORY_DIR = "repository";
    
    private static Repository repository;
    
    protected static Session session;

	protected static JcrSiteStore siteStore;

	protected static JcrArticleStore articleStore;

	protected static JcrArticleSearchService articleSearchService;
	
	protected static JcrWorkingSetStore workingSetStore;

	protected static DummyArticleChangeListener listener = new DummyArticleChangeListener();
    
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (String child : file.list()) {
                deleteFile(new File(file, child));
            }
        }
        if (!file.delete()) {
        	throw new RuntimeException("Could not delete repository!");
        }
    }
    
    private static void deleteTestRepository() {
        File testDir = new File(TEST_REPOSITORY_DIR);
        if (testDir.exists()) {
            deleteFile(testDir);
        }
    }
    
    protected static void setupSession() throws LoginException, RepositoryException, StoreException {
        deleteTestRepository();
        repository = new TransientRepository(new File(TEST_REPOSITORY_DIR));
        session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
        JcrStoreInitializer init = new JcrStoreInitializer();
        init.setSession(session);
        init.registerNodeTypes();
    }
    
    protected static void closeSession() {
        if (session != null) {
            session.logout();
        }
        deleteTestRepository();
    }

	@BeforeClass
	public static void setupStores() throws LoginException, RepositoryException, StoreException {
		    setupSession();
		    workingSetStore = new JcrWorkingSetStore(session);
		    siteStore = new JcrSiteStore(session);
		    articleStore = new JcrArticleStore(session);
		    articleStore.addArticleChangeListener(listener);
		    articleSearchService = new JcrArticleSearchService(session);
		    listener.reset();
		}

	@AfterClass
	public static void close() {
	    closeSession();
	}

	protected Site createSite(String name) throws StoreException {
	    Site site = new Site();
	    site.setName(name);
	    site.setLocation("http://www.abc.de");
	    siteStore.update(site);
	    return site;
	}

	private Article createArticle(String id, String location, int year,
			int month, int dayOfMonth) {
				Article article = new Article();
			    article.setExternalId(id);
			    article.setLocation(location);
			    Calendar cal = Calendar.getInstance();
			    cal.clear();
			    cal.set(Calendar.YEAR, year);
			    cal.set(Calendar.MONTH, month);
			    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			    article.setPublishedDate(cal);
			    return article;
			}

	private Article createArticle(String id, String location, int year,
			int month, int dayOfMonth, String title, String shortText) {
				Article article = createArticle(id, location, year, month, dayOfMonth);
				article.setTitle(title);
				article.setShortText(shortText);
				return article;
			}

	private Article createArticle(String id, String location, int year,
			int month, int dayOfMonth, String title, String shortText, List<TextBlock> content) {
				Article article = createArticle(id, location, year, month, dayOfMonth, title, shortText);
				article.setContent(content);
				return article;
			}

	protected String updateArticle(Site site, String id, String location,
			int year, int month, int dayOfMonth) throws StoreException {
				return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth)).getKey();
			}

	protected String updateArticle(Site site, String id, String location,
			int year, int month, int dayOfMonth, String title, String shortText)
			throws StoreException {
				return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth, title, shortText)).getKey();
			}

	protected String updateArticle(Site site, String id, String location,
			int year, int month, int dayOfMonth, String title, String shortText,
			List<TextBlock> content) throws StoreException {
				return articleStore.update(site, createArticle(id, location, year, month, dayOfMonth, title, shortText, content)).getKey();
			}
}
