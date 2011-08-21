package com.cee.news.store;

import java.util.List;

import com.cee.news.model.NamedKey;
import com.cee.news.model.WorkingSet;

public interface WorkingSetStore {

    void update(WorkingSet workingSet) throws StoreException;
    
    void rename(String oldName, String newName) throws StoreException;
    
    boolean contains(String name) throws StoreException;
    
    WorkingSet getWorkingSet(String name) throws StoreException;
    
    long getWorkingSetCount() throws StoreException;
    
    List<NamedKey> getWorkingSetsOrderedByName() throws StoreException;
}
