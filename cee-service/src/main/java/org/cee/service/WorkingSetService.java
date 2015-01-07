package org.cee.service;

import java.util.ArrayList;
import java.util.List;

import org.cee.client.workingset.WorkingSetData;
import org.cee.client.workingset.WorkingSetUpdateResult;
import org.cee.client.workingset.WorkingSetUpdateResult.State;
import org.cee.news.model.EntityKey;
import org.cee.news.model.Site;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.SiteStore;
import org.cee.news.store.StoreException;
import org.cee.news.store.WorkingSetStore;

public class WorkingSetService {
	
	private WorkingSetStore workingSetStore;

	private SiteStore siteStore;
	
    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
    
    public List<EntityKey> getWorkingSetsOrderedByName() throws StoreException {
        return workingSetStore.getWorkingSetsOrderedByName();
    }

    public WorkingSetData getWorkingSet(EntityKey workingSetKey) throws StoreException {
        return new WorkingSetData(workingSetStore.getWorkingSet(workingSetKey));
    }
    
    public List<EntityKey> validateSiteLanguages(WorkingSetData wsd) throws StoreException {
    	List<EntityKey> sitesWithDifferentLang = new ArrayList<EntityKey>();
    	String workingSetLang = wsd.getLanguage().getKey().toLowerCase();
    	for (EntityKey siteKey : wsd.getSites()) {
			Site site = siteStore.getSite(siteKey);
			String siteLang = site.getLanguage().toLowerCase();
			if (!(siteLang.startsWith(workingSetLang) || workingSetLang.startsWith(siteLang))) {
				sitesWithDifferentLang.add(siteKey);
			}
		}
    	return sitesWithDifferentLang;
    }
    
    public WorkingSetUpdateResult update(WorkingSetData wsd) throws StoreException {
        String newName = wsd.getNewName();
        String oldName = wsd.getOldName();
        if (wsd.getIsNew() && workingSetStore.getWorkingSet(EntityKey.get(newName)) != null) {
            return new WorkingSetUpdateResult(State.entityExists, null, wsd, null);
        }
        List<EntityKey> sitesWithDifferentLang = validateSiteLanguages(wsd);
        WorkingSetUpdateResult result = new WorkingSetUpdateResult(State.ok, null, wsd, null);
        if (sitesWithDifferentLang.size() > 0) {
        	result.setState(State.siteLanguagesDiffer);
        	result.setSitesWithDifferentLang(sitesWithDifferentLang);
        }
        if (!wsd.getIsNew() && !newName.equals(oldName)) {
            workingSetStore.rename(oldName, newName);
        }
        WorkingSet workingSet = null;
        if (wsd.getIsNew()) {
        	workingSet = new WorkingSet();
        	workingSet.setName(newName);
        } else {
        	workingSet = workingSetStore.getWorkingSet(EntityKey.get(newName));
        }
        workingSet.setSites(wsd.getSites());
        workingSet.setLanguage(wsd.getLanguage().getKey());
        result.setKey(workingSetStore.update(workingSet));
        return result;
    }
    
    public WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey) throws StoreException {
        WorkingSetData workingSet = getWorkingSet(workingSetKey);
        workingSet.getSites().add(siteKey);
        return update(workingSet);
    }
    
    public void deleteWorkingSet(EntityKey workingSetKey) throws StoreException {
    	workingSetStore.delete(workingSetKey);
    }
}
