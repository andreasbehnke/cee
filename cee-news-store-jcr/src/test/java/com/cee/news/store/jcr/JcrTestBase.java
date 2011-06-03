package com.cee.news.store.jcr;

import java.io.File;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;

import com.cee.news.store.StoreException;

public abstract class JcrTestBase {
    
    private final static String TEST_REPOSITORY_DIR = "testRepository";
    
    private static Repository repository;
    
    protected static Session session;
    
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (String child : file.list()) {
                deleteFile(new File(file, child));
            }
        }
        file.delete();
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
}
