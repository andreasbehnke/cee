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
import java.util.List;

import org.cee.store.EntityKey;

public class WorkingSetUpdateResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum State {
		ok,
		entityExists,
		siteLanguagesDiffer
	}
	
	private State state;
	
	private List<EntityKey> sitesWithDifferentLang;
	
	private WorkingSetData workingSetData;

	private EntityKey key;
	
	public WorkingSetUpdateResult() {}
	
	public WorkingSetUpdateResult(State state, List<EntityKey> sitesWithDifferentLang, WorkingSetData workingSetData, EntityKey workingSetKey) {
		this.state = state;
		this.sitesWithDifferentLang = sitesWithDifferentLang;
		this.workingSetData = workingSetData;
		this.key = workingSetKey;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public EntityKey getKey() {
		return key;
	}

	public void setKey(EntityKey key) {
		this.key = key;
	}

	public List<EntityKey> getSitesWithDifferentLang() {
		return sitesWithDifferentLang;
	}

	public void setSitesWithDifferentLang(List<EntityKey> sitesWithDifferentLang) {
		this.sitesWithDifferentLang = sitesWithDifferentLang;
	}

	public WorkingSetData getWorkingSetData() {
		return workingSetData;
	}

	public void setWorkingSetData(WorkingSetData workingSetData) {
		this.workingSetData = workingSetData;
	}

}
