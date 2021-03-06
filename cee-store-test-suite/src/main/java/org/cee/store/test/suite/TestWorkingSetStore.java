package org.cee.store.test.suite;

/*
 * #%L
 * Content Extraction Engine - News Store Test Suite
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.workingset.WorkingSet;
import org.cee.store.workingset.WorkingSetStore;
import org.junit.Test;

public abstract class TestWorkingSetStore extends TestStoreBase {
    
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
    private static final EntityKey SITE_A = EntityKey.get("http://googlewebtoolkit.blogspot.com");
    private static final EntityKey SITE_B = EntityKey.get("siteB");
    private static final EntityKey SITE_C = EntityKey.get("siteC");
    private static final String TESTWORKINGSET = "testworkingset";
    
    @Test
    public void testUpdateWorkingSet() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        WorkingSet ws = new WorkingSet();
        ws.setName(TESTWORKINGSET);
        ws.setLanguage("cz");
        List<EntityKey> sites = new ArrayList<EntityKey>();
        sites.add(SITE_A);
        sites.add(SITE_B);
        sites.add(SITE_C);
        ws.setSites(sites);
        
        EntityKey key = workingSetStore.update(ws);
        ws = workingSetStore.getWorkingSet(key);
        
        assertEquals(TESTWORKINGSET, ws.getName());
        assertEquals("cz", ws.getLanguage());
        sites = ws.getSites();
        assertEquals(3, sites.size());
        assertTrue(sites.contains(SITE_A));
        int indexOfSiteA = sites.indexOf(SITE_A);
        assertEquals(SITE_A.getName(), sites.get(indexOfSiteA).getName());
        assertEquals(SITE_A.getKey(), sites.get(indexOfSiteA).getKey());
        assertTrue(sites.contains(SITE_B));
        assertTrue(sites.contains(SITE_C));
        
        ws.getSites().remove(SITE_B);
        ws.setName(EXPECTED_NAME);
        
        key = workingSetStore.update(ws);
        ws = workingSetStore.getWorkingSet(key);
        
        assertEquals(EXPECTED_NAME, ws.getName());
        assertEquals(2, ws.getSites().size());
        assertTrue(ws.getSites().contains(SITE_A));
        assertTrue(ws.getSites().contains(SITE_C));
    }
        
    @Test
    public void testGetWorkingSetUnknown() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        assertNull(workingSetStore.getWorkingSet(EntityKey.get(UNKOWN_WORKING_SET)));
    }
    
    @Test
    public void testGetWorkingSetsOrderedByName() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        WorkingSet ws = new WorkingSet();
        ws.setName(WORKINGSET_3);
        ws.setLanguage("en");
        workingSetStore.update(ws);
        ws.setName(WORKINGSET_2);
        ws.setLanguage("de");
        workingSetStore.update(ws);
        ws.setName(WORKINGSET_1);
        ws.setLanguage("fr");
        workingSetStore.update(ws);
        
        List<EntityKey> workingSets = workingSetStore.getWorkingSetsOrderedByName();
        assertEquals(WORKINGSET_1, workingSets.get(0).getKey());
        assertEquals(WORKINGSET_2, workingSets.get(1).getKey());
        assertEquals(WORKINGSET_3, workingSets.get(2).getKey());
    }
    
    @Test
    public void testRename() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        WorkingSet ws = new WorkingSet();
        ws.setLanguage("en");
        ws.setName(OLD_NAME);
        workingSetStore.update(ws);
        workingSetStore.rename(OLD_NAME, NEW_NAME);
        assertNull(workingSetStore.getWorkingSet(EntityKey.get(OLD_NAME)));
        assertEquals(NEW_NAME, workingSetStore.getWorkingSet(EntityKey.get(NEW_NAME)).getName());
    }
    
    @Test
    public void testDelete() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        WorkingSet ws = new WorkingSet();
        ws.setLanguage("en");
        ws.setName(WORKINGSET_1);
        EntityKey key = workingSetStore.update(ws);
        assertTrue(workingSetStore.contains(key.getName()));
        workingSetStore.delete(key);
        assertFalse(workingSetStore.contains(key.getName()));
    }
    
    @Test
    public void testGetWorkingSetCount() throws StoreException {
    	WorkingSetStore workingSetStore = getWorkingSetStore();
        long startCount = workingSetStore.getWorkingSetCount();
        WorkingSet ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_1);
        ws.setLanguage("en");
        workingSetStore.update(ws);
        ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_2);
        ws.setLanguage("en");
        workingSetStore.update(ws);
        ws = new WorkingSet();
        ws.setName(WORKINGSET_COUNT_3);
        ws.setLanguage("en");
        workingSetStore.update(ws);
        assertEquals(EXPECTED_COUNT, workingSetStore.getWorkingSetCount() - startCount);
    }
}
