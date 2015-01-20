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


import java.util.List;

import org.cee.client.workingset.WorkingSetData;
import org.cee.service.DuplicateKeyException;
import org.cee.service.workingset.WorkingSetService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.webreader.client.error.ServiceException;
import org.cee.webreader.client.workingset.GwtWorkingSetService;
import org.cee.webreader.client.workingset.WorkingSetUpdateResult;
import org.cee.webreader.client.workingset.WorkingSetUpdateResult.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkingSetServiceImpl implements GwtWorkingSetService {
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkingSetServiceImpl.class);

    private static final String COULD_NOT_VALIDATE_SITE_LANGUAGES = "Could not validate site languages";

	private static final String COULD_NOT_UPDATE_WORKING_SET = "Could not update working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET = "Could not retrieve working set";

	private static final String COULD_NOT_RETRIEVE_WORKING_SET_LIST = "Could not retrieve working set list";
	
	private static final String COULD_NOT_DELETE_WORKING_SET = "Could not delete working set";

	private WorkingSetService workingSetService;
	
    public void setWorkingSetService(WorkingSetService workingSetService) {
		this.workingSetService = workingSetService;
	}
    
    @Override
    public List<EntityKey> getWorkingSetsOrderedByName() {
        try {
            return workingSetService.orderedByName();
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET_LIST);
        }
    }

    @Override
    public WorkingSetData getWorkingSet(EntityKey workingSetKey) {
        try {
        	return workingSetService.get(workingSetKey);
        } catch (Exception e) {
        	LOG.error(COULD_NOT_RETRIEVE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_WORKING_SET);
        }
    }
    
    @Override
    public List<EntityKey> validateSiteLanguages(WorkingSetData wsd) {
    	try {
	    	return workingSetService.validateSiteLanguages(wsd);
	    } catch (StoreException e) {
			LOG.error(COULD_NOT_VALIDATE_SITE_LANGUAGES, e);
	        throw new ServiceException(COULD_NOT_VALIDATE_SITE_LANGUAGES);
		}
    }
    
    @Override
    public WorkingSetUpdateResult update(WorkingSetData wsd) {
        try {
        	WorkingSetData workingSetData = workingSetService.update(wsd);
            return new WorkingSetUpdateResult(State.ok, null, workingSetData, EntityKey.get(workingSetData.getNewName()));
        } catch (StoreException e) {
        	LOG.error(COULD_NOT_UPDATE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_UPDATE_WORKING_SET);
        } catch (DuplicateKeyException e) {
			return new WorkingSetUpdateResult(State.entityExists, null, wsd, e.getEntityKey());
		}
    }
    
    @Override
    public WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey) {
        try {
        	WorkingSetData workingSetData = workingSetService.addSite(workingSetKey, siteKey);
        	return new WorkingSetUpdateResult(State.ok, null, workingSetData, EntityKey.get(workingSetData.getNewName()));
        } catch (Exception e) {
        	LOG.error(COULD_NOT_UPDATE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_UPDATE_WORKING_SET);
        }
    }
    
    @Override
    public void deleteWorkingSet(EntityKey workingSetKey) {
    	try {
    		workingSetService.delete(workingSetKey);
		} catch (Exception e) {
			LOG.error(COULD_NOT_DELETE_WORKING_SET, e);
            throw new ServiceException(COULD_NOT_DELETE_WORKING_SET);
		}
    }
}
