package com.cee.news.store.lucene;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;

public class LuceneSiteStore implements SiteStore {

	@Override
	public EntityKey update(Site site) throws StoreException {
		return null;
	}

	@Override
	public boolean contains(String name) throws StoreException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Site getSite(EntityKey key) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getSites(List<EntityKey> keys) throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntityKey> getSitesOrderedByName() throws StoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
