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
public class SiteUpdateCommand extends AbstractCommand {
	
	private static final Logger log = LoggerFactory.getLogger(SiteUpdateCommand.class);
	
	private SiteUpdater siteUpdater;
	
	private Site site;

	public void setSite(Site site) {
		this.site = site;
	}
	
	public void setSiteUpdater(SiteUpdater updater) {
		this.siteUpdater = updater;
	}
	
	@Override
	public int getRemainingTasks() {
		if (siteUpdater == null) {
			throw new IllegalStateException("Missing site updater");
		}
		return siteUpdater.getRemainingArticles();
	}
	
	@Override
	protected void runInternal() {
		if (siteUpdater == null) {
			throw new IllegalStateException("Missing site updater");
		}
		if (site == null) {
			throw new IllegalStateException("Missing site to update");
		}
		
		try {
			log.info("Updating site {} at location {}", site.getName(), site.getLocation());
			siteUpdater.update(site);
			log.info("Finished updating site {} at location {}", site.getName(), site.getLocation());
		} catch (ParserException e) {
			log.error("Could not parse articles of site", e);
			throw new RuntimeException(e);
		} catch (StoreException e) {
			log.error("Could not store articles of site", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("Could not read site or site article", e);
			throw new RuntimeException(e);
		}
	}
}
