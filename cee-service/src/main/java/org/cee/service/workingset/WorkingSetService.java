package org.cee.service.workingset;

/*
 * #%L
 * Content Extraction Engine - Services
 * %%
 * Copyright (C) 2013 - 2015 Andreas Behnke
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.cee.client.workingset.WorkingSetData;
import org.cee.service.DuplicateKeyException;
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
	
	private Validator validator;
	
	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
    
    public void setValidator(Validator validator) {
		this.validator = validator;
	}
    
    public List<EntityKey> orderedByName() throws StoreException {
        return workingSetStore.getWorkingSetsOrderedByName();
    }

    public WorkingSetData get(EntityKey workingSetKey) throws StoreException, EntityNotFoundException {
    	WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
    	if (ws == null) {
    		throw new EntityNotFoundException(workingSetKey);
    	}
        return new WorkingSetData(ws, false);
    }
    
    public List<EntityKey> validateSiteLanguages(WorkingSetData wsd) throws StoreException {
    	List<EntityKey> sitesWithDifferentLang = new ArrayList<EntityKey>();
    	if (wsd.getSites() != null) {
	    	String workingSetLang = wsd.getLanguage().getKey().toLowerCase();
	    	for (EntityKey siteKey : wsd.getSites()) {
				Site site = siteStore.getSite(siteKey);
				String siteLang = site.getLanguage().toLowerCase();
				if (!(siteLang.startsWith(workingSetLang) || workingSetLang.startsWith(siteLang))) {
					sitesWithDifferentLang.add(siteKey);
				}
			}
    	}
    	return sitesWithDifferentLang;
    }
    
    public WorkingSetData update(WorkingSetData wsd) throws StoreException, DuplicateKeyException, ConstraintViolationException {
    	Set<ConstraintViolation<WorkingSetData>> issues = validator.validate(wsd);
    	if (!issues.isEmpty()) {
    		throw new ConstraintViolationException(issues);
    	}
    	String newName = wsd.getNewName();
        String oldName = wsd.getOldName();
        EntityKey newKey = EntityKey.get(newName);
        if (wsd.getIsNew() && workingSetStore.getWorkingSet(newKey) != null) {
        	throw new DuplicateKeyException(newKey);
        }
        if (!wsd.getIsNew() && !newName.equals(oldName)) {
        	if (workingSetStore.getWorkingSet(newKey) != null) {
        		throw new DuplicateKeyException(newKey);
        		
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
        workingSetStore.update(workingSet);
        return new WorkingSetData(workingSet, wsd.getIsNew());
    }
    
    public WorkingSetData addSite(EntityKey workingSetKey, EntityKey siteKey) throws StoreException, EntityNotFoundException, DuplicateKeyException {
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
