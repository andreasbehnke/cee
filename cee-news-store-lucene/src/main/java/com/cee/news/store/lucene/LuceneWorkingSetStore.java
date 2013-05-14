package com.cee.news.store.lucene;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class LuceneWorkingSetStore implements WorkingSetStore {

	@Override
	public EntityKey update(WorkingSet workingSet) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rename(String oldName, String newName) throws StoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(EntityKey key) throws StoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(String name) throws StoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WorkingSet getWorkingSet(EntityKey key) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWorkingSetCount() throws StoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<EntityKey> getWorkingSetsOrderedByName() throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
