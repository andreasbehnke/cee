package com.cee.news.server.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.async.EntityUpdateResult.State;
import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.client.list.EntityContent;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class SiteServiceImpl implements SiteService {

	private static final String PREFIX_HTTP_WWW = "http://www.";

	private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

	private static final String COULD_NOT_RETRIEVE_SITE_DESCRIPTION = "Could not retrieve site description";

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

	@Override
	public List<EntityKey> getSites() {
		try {
			return siteStore.getSitesOrderedByName();
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
		}
	}
	
	@Override
	public List<EntityKey> getSitesOfWorkingSet(String workingSetName) {
		try {
			return workingSetStore.getWorkingSet(workingSetName).getSites();
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
		}
	}

	protected EntityContent renderDescription(EntityKey key, Site site) {
		StringBuilder builder = new StringBuilder();
		String title = site.getTitle();
		String description = site.getDescription();
		builder.append("<p>").append(title == null ? site.getLocation() : title).append("</p>")
		.append("<p>").append(site.getName()).append("</p>");
		if (description != null) {
			builder.append("<p>").append(site.getDescription()).append("</p>");
		}
		return new EntityContent(key, builder.toString());
	}
	
	@Override
	public EntityContent getHtmlDescription(EntityKey siteKey) {
		try {
			Site site = siteStore.getSite(siteKey.getKey());
			return renderDescription(siteKey, site);
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
		}
	}
	
	@Override
	public List<EntityContent> getHtmlDescriptions(List<EntityKey> keys) {
		try {
			List<EntityContent> descriptions = new ArrayList<EntityContent>();
			for (EntityKey key : keys) {
				Site site = siteStore.getSite(key.getKey());
				descriptions.add(renderDescription(key, site));
			}
			return descriptions;
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
		}
	}

	@Override
	public String guessUniqueSiteName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Paramter name must not be null");
		}
		int counter = 1;
		String guessName = name;
		if (guessName.startsWith(PREFIX_HTTP_WWW)) {
			guessName = guessName.substring(PREFIX_HTTP_WWW.length());
			int point = guessName.indexOf('.');
			if (point>0) {
				guessName = guessName.substring(0, point);
			}
		}
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
				return new EntityUpdateResult(State.entityExists, null);
			}
			EntityKey key = siteStore.update(SiteConverter.createFromSiteData(siteData));
			return new EntityUpdateResult(State.ok, key);
		} catch (StoreException e) {
			log.error(COULD_NOT_UPDATE_SITE, e);
			throw new ServiceException(COULD_NOT_UPDATE_SITE);
		}
	}
}
