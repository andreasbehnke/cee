package org.cee.service.site;

import java.util.List;

import org.cee.client.site.FeedData;
import org.cee.client.site.SiteConverter;
import org.cee.client.site.SiteData;
import org.cee.client.site.SiteUpdateResult;
import org.cee.client.site.SiteUpdateResult.State;
import org.cee.news.model.EntityKey;
import org.cee.news.model.Site;
import org.cee.news.store.SiteStore;
import org.cee.news.store.StoreException;
import org.cee.news.store.WorkingSetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);

    private static final String PREFIX_HTTP_WWW = "http://www.";

    private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

	private SiteStore siteStore;

    private WorkingSetStore workingSetStore;

    public void setSiteStore(SiteStore siteStore) {
        this.siteStore = siteStore;
    }

    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }

    public List<EntityKey> orderedByName() throws StoreException {
    	return siteStore.getSitesOrderedByName();
    }

    public List<EntityKey> sitesOfWorkingSet(EntityKey workingSetKey) throws StoreException {
        return workingSetStore.getWorkingSet(workingSetKey).getSites();
    }
    
    public Site get(EntityKey siteKey) throws StoreException {
    	return siteStore.getSite(siteKey);
    }
    
    public List<Site> get(List<EntityKey> siteKeys) throws StoreException {
    	return siteStore.getSites(siteKeys);
    }

    public String guessUniqueSiteName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Paramter name must not be null");
        }
        int counter = 1;
        String guessName = null;
        if (name.startsWith(PREFIX_HTTP_WWW)) {
            name = name.substring(PREFIX_HTTP_WWW.length());
            int point = name.indexOf('.');
            if (point > 0) {
                name = name.substring(0, point);
            }
        }
        try {
        	guessName = name;
            while (siteStore.contains(guessName)) {
                guessName = name + " " + counter;
                counter++;
            }
        } catch (Exception e) {
            LOG.error(COULD_NOT_GUESS_SITE_NAME, e);
            throw new RuntimeException(COULD_NOT_GUESS_SITE_NAME);
        }
        return guessName;
    }

    public SiteUpdateResult update(SiteData siteData) throws StoreException {
        if (siteStore.contains(siteData.getName()) && siteData.getIsNew()) {
            return new SiteUpdateResult(State.entityExists, null);
        }
        EntityKey language = siteData.getLanguage();
        if (language == null)  {
        	return new SiteUpdateResult(State.languageMissing, null);
        }
        //set language for all feeds 
        for (FeedData feedData : siteData.getFeeds()) {
            if (feedData.getLanguage() == null) {
            	//if feed does not provide language information,
            	//set language to site's language
            	feedData.setLanguage(language);
            }
        }
        EntityKey key = siteStore.update(SiteConverter.createFromSiteData(siteData));
        return new SiteUpdateResult(State.ok, key);
    }
}
