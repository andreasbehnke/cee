package com.cee.news.client.content;

import java.util.List;

import com.cee.news.client.async.EntityUpdateResult;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtSiteService")
public interface SiteService extends RemoteService {

	/**
	 * @return List of all available sites names
	 */
	List<String> getSites();
	
	/**
	 * @param workingSetName Unique name of working set
	 * @return List of site names of given working set
	 */
	List<String> getSitesOfWorkingSet(String workingSetName);
	
	/**
	 * Retrieve the sites formatted title
	 * @param name The name of the site
	 * @return The HTML formatted site title
	 */
	SafeHtml getTitle(String name);
	
	/**
	 * Retrieve the sites formatted description
	 * @param name The name of the site
	 * @return The HTML formatted site description
	 */
	SafeHtml getHtmlDescription(String name);
	
	/**
	 * Guesses a unique site name for a given site name.
	 * If the name does not exists, the name will be returned.
	 * Otherwise a number will be incremented and appended to name 
	 * until the name does not math any existing name.
	 * @param name The user's name
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
