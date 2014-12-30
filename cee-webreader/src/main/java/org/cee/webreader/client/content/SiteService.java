package org.cee.webreader.client.content;

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

import org.cee.news.model.EntityKey;
import org.cee.processing.site.model.SiteData;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtSiteService")
public interface SiteService extends RemoteService {

	/**
	 * @return List of all available sites names
	 */
	List<EntityKey> getSites();
	
	/**
	 * @param workingSetKey Unique name of working set
	 * @return List of site names of given working set
	 */
	List<EntityKey> getSitesOfWorkingSet(EntityKey workingSetKey);
	
	/**
	 * Retrieve the sites formatted description
	 */
	EntityContent<EntityKey> getHtmlDescription(EntityKey key);
	
	/**
	 * Retrieve formatted site description for the given site keys
	 */
	List<EntityContent<EntityKey>> getHtmlDescriptions(ArrayList<EntityKey> keys);
	
	/**
	 * Guesses a unique site name for a given site name.
	 * If the name does not exists, the name will be returned.
	 * Otherwise a number will be incremented and appended to name 
	 * until the name does not match any existing name.
	 * @param name The name to test
	 * @return A unique name
	 */
	String guessUniqueSiteName(String name);
	
	/**
	 * Updates or creates a site
	 * @param site to update or create
	 * @return the result state of the operation
	 */ 
	SiteUpdateResult update(SiteData site);
}
