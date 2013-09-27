package com.cee.news.store;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;

public interface WorkingSetStore {

    EntityKey update(WorkingSet workingSet) throws StoreException;
    
    void rename(String oldName, String newName) throws StoreException;
    
    void delete(EntityKey key) throws StoreException;
    
    boolean contains(String name) throws StoreException;
    
    WorkingSet getWorkingSet(EntityKey key) throws StoreException;
    
    long getWorkingSetCount() throws StoreException;
    
    List<EntityKey> getWorkingSetsOrderedByName() throws StoreException;
}
