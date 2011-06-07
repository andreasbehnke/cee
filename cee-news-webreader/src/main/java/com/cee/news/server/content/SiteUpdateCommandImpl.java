/**
 * 
 */
package com.cee.news.server.content;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Site;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.SiteUpdater;
import com.cee.news.store.StoreException;

/**
 * Work item which uses a {@link SiteUpdater} internally
 */
public class SiteUpdateCommandImpl implements SiteUpdateCommand {
	
	private static final Logger log = LoggerFactory.getLogger(SiteUpdateCommandImpl.class);
	
	private SiteUpdater siteUpdater;
	
	private Site site;

	@Override
	public void setSite(Site site) {
		this.site = site;
	}
	
	public void setSiteUpdater(SiteUpdater updater) {
		this.siteUpdater = updater;
	}
	
	@Override
	public void run() {
		if (siteUpdater == null) {
			throw new IllegalStateException("Missing site updater");
		}
		if (site == null) {
			throw new IllegalStateException("Missing site to update");
		}
		
		try {
			siteUpdater.update(site);
		} catch (ParserException e) {
			log.error("Could not parse articles of site", e);
		} catch (StoreException e) {
			log.error("Could not store articles of site", e);
		} catch (IOException e) {
			log.error("Could not read site or site article", e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SiteUpdateCommandImpl))
			return false;
		SiteUpdateCommandImpl other = (SiteUpdateCommandImpl) obj;
		return site.getLocation().equals(other.site.getLocation());
	}	
}
