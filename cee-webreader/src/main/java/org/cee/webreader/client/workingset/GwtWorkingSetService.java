package org.cee.webreader.client.workingset;

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
import org.cee.client.workingset.WorkingSetUpdateResult;
import org.cee.store.EntityKey;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtWorkingSetService")
public interface GwtWorkingSetService extends RemoteService {
	
	List<EntityKey> getWorkingSetsOrderedByName();
	
	WorkingSetData getWorkingSet(EntityKey workingSetKey);
	
	List<EntityKey> validateSiteLanguages(WorkingSetData workingSet);
	
	WorkingSetUpdateResult update(WorkingSetData workingSet);
	
	WorkingSetUpdateResult addSiteToWorkingSet(EntityKey workingSetKey, EntityKey siteKey);
	
	void deleteWorkingSet(EntityKey workingSetKey);
}
