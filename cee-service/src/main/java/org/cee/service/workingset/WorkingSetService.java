package org.cee.service.workingset;

import java.util.ArrayList;
import java.util.List;

import org.cee.client.workingset.WorkingSetData;
import org.cee.client.workingset.WorkingSetUpdateResult;
import org.cee.client.workingset.WorkingSetUpdateResult.State;
import org.cee.service.EntityNotFoundException;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.site.Site;
import org.cee.store.site.SiteStore;
import org.cee.store.workingset.WorkingSet;
import org.cee.store.workingset.WorkingSetStore;

public class WorkingSetService {
	
	private WorkingSetStore workingSetStore;

	private SiteStore siteStore;
	
    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
    
    public List<EntityKey> orderedByName() throws StoreException {
        return workingSetStore.getWorkingSetsOrderedByName();
    }

    public WorkingSetData get(EntityKey workingSetKey) throws StoreException, EntityNotFoundException {
    	WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
    	if (ws == null) {
    		throw new EntityNotFoundException(workingSetKey);
    	}
        return new WorkingSetData(ws);
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
        EntityKey newKey = EntityKey.get(newName);
        if (wsd.getIsNew() && workingSetStore.getWorkingSet(newKey) != null) {
            return new WorkingSetUpdateResult(State.entityExists, null, wsd, newKey);
        }
        List<EntityKey> sitesWithDifferentLang = validateSiteLanguages(wsd);
        WorkingSetUpdateResult result = new WorkingSetUpdateResult(State.ok, null, wsd, newKey);
        if (sitesWithDifferentLang.size() > 0) {
        	result.setState(State.siteLanguagesDiffer);
        	result.setSitesWithDifferentLang(sitesWithDifferentLang);
        }
        if (!wsd.getIsNew() && !newName.equals(oldName)) {
        	if (workingSetStore.getWorkingSet(newKey) != null) {
        		return new WorkingSetUpdateResult(State.entityExists, null, wsd, newKey);
        	}
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
    
    public WorkingSetUpdateResult addSite(EntityKey workingSetKey, EntityKey siteKey) throws StoreException, EntityNotFoundException {
        WorkingSetData workingSet = get(workingSetKey);
        workingSet.getSites().add(siteKey);
        return update(workingSet);
    }
    
    public void delete(EntityKey workingSetKey) throws StoreException, EntityNotFoundException {
    	if (!workingSetStore.contains(workingSetKey.getKey())) {
    		throw new EntityNotFoundException(workingSetKey);
    	}
    	workingSetStore.delete(workingSetKey);
    }
}
