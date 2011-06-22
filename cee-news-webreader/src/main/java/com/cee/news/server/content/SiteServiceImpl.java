package com.cee.news.server.content;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.async.EntityUpdateResult;
import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;

public class SiteServiceImpl implements SiteService {

	private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

	private static final String COULD_NOT_RETRIEVE_SITE_DESCRIPTION = "Could not retrieve site description";

	private static final String COULD_NOT_RETRIEVE_SITE_TITLE = "Could not retrieve site title";

	private static final String COULD_NOT_RETRIEVE_SITE_LIST = "Could not retrieve site list";

	private static final String COULD_NOT_UPDATE_SITE = "Could not update site";

	private static final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

	private SiteStore siteStore;
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}

	public List<String> getSites() {
		try {
			return siteStore.getSitesOrderedByName();
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
		}
	}

	public SafeHtml getTitle(String siteName) {
		try {
			Site site = siteStore.getSite(siteName);
			SafeHtmlBuilder builder = new SafeHtmlBuilder();
			String title = site.getTitle();
			builder.appendEscaped(title == null ? siteName : title);
			return builder.toSafeHtml();
		} catch (StoreException e) {
			log.error(COULD_NOT_RETRIEVE_SITE_TITLE, e);
			throw new ServiceException(COULD_NOT_RETRIEVE_SITE_TITLE);
		}
	}

	public SafeHtml getHtmlDescription(String siteName) {
		try {
			Site site = siteStore.getSite(siteName);
			SafeHtmlBuilder builder = new SafeHtmlBuilder();
			String title = site.getTitle();
			String description = site.getDescription();
			builder.appendHtmlConstant("<h3>");
			builder.appendEscaped(title == null ? site.getLocation() : title);
			builder.appendHtmlConstant("</h3>");
			if (description != null) {
				builder.appendHtmlConstant("<div class=\"description\">");
				builder.append(SimpleHtmlSanitizer.sanitizeHtml(site
						.getDescription()));
				builder.appendHtmlConstant("</div>");
			}
			return builder.toSafeHtml();
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
