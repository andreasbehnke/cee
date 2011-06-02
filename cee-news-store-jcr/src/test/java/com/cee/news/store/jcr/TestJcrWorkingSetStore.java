package com.cee.news.store.jcr;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;

public class TestJcrWorkingSetStore extends JcrTestBase {
    
    private static final String NEW_NAME = "newName";
    private static final String OLD_NAME = "oldName";
    private static final String WORKINGSET_1 = "aaa";
    private static final String WORKINGSET_2 = "aab";
    private static final String WORKINGSET_3 = "aba";
    private static final String WORKINGSET_COUNT_1 = "count1";
    private static final String WORKINGSET_COUNT_2 = "count2";
    private static final String WORKINGSET_COUNT_3 = "count3";
    private static final long EXPECTED_COUNT = 3;
    private static final String UNKOWN_WORKING_SET = "unkown";
    private static final String EXPECTED_NAME = "abc";
    private static final String SITE_C = "siteC";
    private static final String SITE_B = "siteB";
    private static final String SITE_A = "siteA";
    private static final String TESTWORKINGSET = "testworkingset";
    private static JcrWorkingSetStore workingSetStore;
    
    @BeforeClass
    public static void setupStores() throws LoginException, RepositoryException, StoreException {
        setupSession();
        workingSetStore = new JcrWorkingSetStore(session);
    }
    
    @AfterClass
    public static void close() {
        closeSession();
    }
    
    @Test
    public void testUpdateWorkingSet() throws StoreException {
        WorkingSet ws = new WorkingSet();
        ws.setName(TESTWORKINGSET);
        List<String> sites = new ArrayList<String>();
        sites.add(SITE_A);
        sites.add(SITE_B);
        sites.add(SITE_C);
        ws.setSites(sites);
        
        workingSetStore.update(ws);
        ws = workingSetStore.getWorkingSet(TESTWORKINGSET);
        
        assertEquals(TESTWORKINGSET, ws.getName());
        assertEquals(3, ws.getSites().size());
        assertTrue(ws.getSites().contains(SITE_A));
        assertTrue(ws.getSites().contains(SITE_B));
        assertTrue(ws.getSites().contains(SITE_C));
        
        ws.getSites().remove(SITE_B);
        ws.setName(EXPECTED_NAME);
        
        workingSetStore.update(ws);
        ws = workingSetStore.getWorkingSet(EXPECTED_NAME);
        
        assertEquals(EXPECTED_NAME, ws.getName());
        assertEquals(2, ws.getSites().size());
        assertTrue(ws.getSites().contains(SITE_A));
        assertTrue(ws.getSites().contains(SITE_C));
    }
        
    @Test
    public void testGetWorkingSetUnknown() throws StoreException {
        assertNull( workingSetStore.getWorkingSet(UNKOWN_WORKING_SET) );
    }
    
    @Test
    public void testGetWorkingSetsOrderedByName() throws StoreException {
        WorkingSet ws = new WorkingSet();
        ws.setName(WORKINGSET_3);
        workingSetStore.update(ws);
        ws.setName(WORKINGSET_2);
        workingSetStore.update(ws);
        ws.setName(WORKINGSET_1);
        workingSetStore.update(ws);
        
        List<String> workingSets = workingSetStore.getWorkingSetsOrderedByName();
        assertEquals(WORKINGSET_1, workingSets.get(0));
        assertEquals(WORKINGSET_2, workingSets.get(1));
        assertEquals(WORKINGSET_3, workingSets.get(2));
    }
    
    @Test
    public void testRename() throws StoreException {
        WorkingSet ws = new WorkingSet();
        ws.setName(OLD_NAME);
        workingSetStore.update(ws);
        workingSetStore.rename(OLD_NAME, NEW_NAME);
        assertNull(workingSetStore.getWorkingSet(OLD_NAME));
        assertEquals(NEW_NAME, workingSetStore.getWorkingSet(NEW_NAME).getName());
    }
    
    @Test
    public void testGetWorkingSetCount() throws StoreException {
        long startCount = workingSetStore.getWorkingSetCount();
        WorkingSet ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_1);
        workingSetStore.update(ws);
        ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_2);
        workingSetStore.update(ws);
        ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_3);
        workingSetStore.update(ws);
        assertEquals(EXPECTED_COUNT, workingSetStore.getWorkingSetCount() - startCount);
    }
}
