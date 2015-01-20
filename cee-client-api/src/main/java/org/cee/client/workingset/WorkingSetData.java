package org.cee.client.workingset;

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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.cee.store.EntityKey;
import org.cee.store.workingset.WorkingSet;

/**
 * Bean holding all view data of a working set
 */
public class WorkingSetData implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private Boolean isNew = true;

	private String newName;
    
    private String oldName;
    
    private EntityKey language;
    
    private List<EntityKey> sites = new ArrayList<EntityKey>();
    
    public WorkingSetData() {}
    
    public WorkingSetData(WorkingSet workingSet, boolean isNew) {
    	this.isNew = isNew;
    	this.newName = workingSet.getName();
    	this.oldName = workingSet.getName();
    	this.sites = workingSet.getSites();
    	this.language = EntityKey.get(workingSet.getLanguage());
    }

    /**
     * @return If true, this bean is a new created working set, otherwise this working set should be updated
     */
    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return The new name of this working set
     */
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    /**
     * @return The old name of the working set, used by the backend service for renaming
     */
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    /**
     * @return Site URL's of this working set
     */
    public List<EntityKey> getSites() {
        return sites;
    }

    public void setSites(List<EntityKey> sites) {
        this.sites = sites;
    }

	public EntityKey getLanguage() {
		return language;
	}

	public void setLanguage(EntityKey language) {
		this.language = language;
	}
}
