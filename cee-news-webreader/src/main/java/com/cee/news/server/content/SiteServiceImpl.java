package com.cee.news.server.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.client.list.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class SiteServiceImpl implements SiteService {

	private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

	private static final String COULD_NOT_RETRIEVE_SITE_DESCRIPTION = "Could not retrieve site description";

	private static final String COULD_NOT_RETRIEVE_SITE_TITLE = "Could not retrieve site title";

	private static final String COULD_NOT_RETRIEVE_SITE_LIST = "Could not retrieve site list";

	private static final String COULD_NOT_UPDATE_SITE = "Could not update site";

	private static final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

	private SiteStore siteStore;
	
	private WorkingSetStore workingSetStore;
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
	
	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}
	
	protected List<EntityKey> buildSiteKeys(List<String> names) {
		List<EntityKey> keys = new ArrayList<EntityKey>();
		for (String name : names) {
			keys.add(new EntityKey(name, name));
		}
		return keys;
	}

	public List<EntityKey> getSites() {
		try {
			return buildSiteKeys(siteStore.getSitesOrderedByName());
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
		}
	}
	
	@Override
	public List<EntityKey> getSitesOfWorkingSet(String workingSetName) {
		try {
			return buildSiteKeys(workingSetStore.getWorkingSet(workingSetName).getSites());
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
		}
	}

	//TODO: Use Velocity Template Engine
	public String getHtmlTitle(String siteName) {
		try {
			Site site = siteStore.getSite(siteName);
			String title = site.getTitle();
			return (title == null ? siteName : title);
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_TITLE, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_TITLE);
		}
	}

	//TODO: Use Velocity Template Engine
	public String getHtmlDescription(String siteName) {
		try {
			Site site = siteStore.getSite(siteName);
			StringBuilder builder = new StringBuilder();
			String title = site.getTitle();
			String description = site.getDescription();
			builder.append("<p>").append(title == null ? site.getLocation() : title).append("</p>")
			.append("<p>").append(site.getName()).append("</p>");
			
			if (description != null) {
				builder.append("<p>").append(site.getDescription()).append("</p>");
			}
			return builder.toString();
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
		}
	}

	@Override
	public String guessUniqueSiteName(String name) {
		int counter = 1;
		String guessName = name;
		try {
			while(siteStore.contains(guessName)) {
				guessName = name + " " + counter;
				counter++;
			}
		} catch (StoreException e) {
			log.error(COULD_NOT_GUESS_SITE_NAME, e);
			throw new ServiceException(COULD_NOT_GUESS_SITE_NAME);
		}
		return guessName;
	}
	
	@Override
	public EntityUpdateResult update(SiteData siteData) {
		try {
			if (siteStore.contains(siteData.getName()) && siteData.getIsNew()) {
				return EntityUpdateResult.entityExists;
			}
			siteStore.update(SiteConverter.createFromSiteData(siteData));
			return EntityUpdateResult.ok;
		} catch (StoreException e) {
			log.error(COULD_NOT_UPDATE_SITE, e);
			throw new ServiceException(COULD_NOT_UPDATE_SITE);
		}
	}
}
