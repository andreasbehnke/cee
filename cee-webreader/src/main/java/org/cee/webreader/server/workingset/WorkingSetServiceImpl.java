package org.cee.webreader.server.workingset;

/*
 * #%L
 * News Reader
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
import org.cee.webreader.client.error.ServiceException;
import org.cee.webreader.client.workingset.GwtWorkingSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkingSetServiceImpl implements GwtWorkingSetService {
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkingSetServiceImpl.class);

    private static final String COULD_NOT_VALIDATE_SITE_LANGUAGES = "Could not validate site languages";

	private static final String COULD_NOT_UPDATE_WORKING_SET = "Could not update working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET = "Could not retrieve working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET_LIST = "Could not retrieve working set list";
	
	private static final String COULD_NOT_DELETE_WORKING_SET = "Could not delete working set";

	private WorkingSetStore workingSetStore;

	private SiteStore siteStore;
	
    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
    
    @Override
    public List<EntityKey> getWorkingSetsOrderedByName() {
        try {
            return workingSetStore.getWorkingSetsOrderedByName();
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET_LIST);
        }
    }

    @Override
    public WorkingSetData getWorkingSet(EntityKey workingSetKey) {
        try {
            WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetKey);
            WorkingSetData wsd = new WorkingSetData();
            wsd.setIsNew(false);
            wsd.setNewName(workingSet.getName());
            wsd.setOldName(workingSet.getName());
            wsd.setSites(workingSet.getSites());
            wsd.setLanguage(EntityKey.get(workingSet.getLanguage()));
            return wsd;
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET);
        }
    }
    
    @Override
    public List<EntityKey> validateSiteLanguages(WorkingSetData wsd) {
    	try {
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
	    } catch (StoreException e) {
			LOG.error(COULD_NOT_VALIDATE_SITE_LANGUAGES, e);
	        throw new ServiceException(COULD_NOT_VALIDATE_SITE_LANGUAGES);
		}
    }
    
    @Override
    public WorkingSetUpdateResult update(WorkingSetData wsd) {
        try {
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
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_UPDATE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_UPDATE_WORKING_SET);
        }
    }
    
    @Override
    public WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey) {
        WorkingSetData workingSet = getWorkingSet(workingSetKey);
        workingSet.getSites().add(siteKey);
        return update(workingSet);
    }
    
    @Override
    public void deleteWorkingSet(EntityKey workingSetKey) {
    	try {
			workingSetStore.delete(workingSetKey);
		} catch (StoreException e) {
			LOG.error(COULD_NOT_DELETE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_DELETE_WORKING_SET);
		}
    }
}
