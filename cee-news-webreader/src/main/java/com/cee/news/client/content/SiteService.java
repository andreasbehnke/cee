package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.model.EntityKey;
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
	EntityUpdateResult update(SiteData site);
}
