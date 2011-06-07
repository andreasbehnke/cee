/**
 * 
 */
package com.cee.news.server.content;

import com.cee.news.model.Site;

/**
 * Command updates the articles of a site
 */
public interface SiteUpdateCommand extends Runnable {

	void setSite(Site site);
}
