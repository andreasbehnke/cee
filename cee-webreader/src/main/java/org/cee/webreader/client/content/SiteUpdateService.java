package org.cee.webreader.client.content;

import org.cee.news.model.EntityKey;

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
	 * Retrieves all feed information from a given feed url
	 * @param location The feed's location
	 * @return SIte information, either in state ok and the feed information embedded or in error state
	 */
	FeedData retrieveFeedData(String location);
	
	/**
	 * Starts the scheduler which triggers the site updates periodically
	 */
	void startUpdateScheduler();
	
	/**
	 * Adds the site to the update queue, if queue does not contain site. 
	 * The update will be performed as soon as possible.
	 * @param siteKey Key of the site to be updated
	 * @return true, if site has been added or false if site is already queued for update
	 */
	boolean addSiteToUpdateQueue(EntityKey siteKey);
}
