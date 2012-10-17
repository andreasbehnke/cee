package com.cee.news.client.content;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Asynchronously update service. Updates the articles of sites.
 */
@RemoteServiceRelativePath("services/gwtSiteUpdateService")
public interface SiteUpdateService extends RemoteService {
	
	/**
	 * Retrieves all sites information from a given web url
	 * @param location The web URL
	 * @return Site information, either in state ok and the site object embedded or in error state
	 */
	SiteData retrieveSiteData(String location);
	
	/**
	 * Removes all commands from queue
	 */
	void clearQueue();
	
	/**
	 * @return The number of update tasks in the queue. Used for displaying 
	 * a progress bar.
	 */
	int getUpdateTasks();
	
	/**
	 * Starts the scheduler which triggers the site updates periodically
	 */
	void startUpdateScheduler();
	
	/**
	 * Adds the site to the update queue. The update will be performed as soon as possible.
	 * @param siteKey Key of the site to be updated
	 */
	void addSiteToUpdateQueue(EntityKey siteKey);
}
