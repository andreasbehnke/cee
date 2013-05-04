package com.cee.news.server.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteService;
import com.cee.news.client.content.SiteUpdateResult;
import com.cee.news.client.content.SiteUpdateResult.State;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.server.content.renderer.SiteContentRenderer;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;

public class SiteServiceImpl implements SiteService {

    private static final Logger LOG = LoggerFactory.getLogger(SiteServiceImpl.class);

    private static final String PREFIX_HTTP_WWW = "http://www.";

    private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

    private static final String COULD_NOT_RETRIEVE_SITE_DESCRIPTION = "Could not retrieve site description";

    private static final String COULD_NOT_RETRIEVE_SITE_LIST = "Could not retrieve site list";

    private static final String COULD_NOT_UPDATE_SITE = "Could not update site";

    private SiteStore siteStore;

    private WorkingSetStore workingSetStore;

    private SiteContentRenderer renderer = new SiteContentRenderer();

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
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
        }
    }

    @Override
    public List<EntityKey> getSitesOfWorkingSet(EntityKey workingSetKey) {
        try {
            return workingSetStore.getWorkingSet(workingSetKey).getSites();
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
        }
    }

    @Override
    public EntityContent<EntityKey> getHtmlDescription(EntityKey siteKey) {
        try {
            Site site = siteStore.getSite(siteKey);
            return renderer.render(siteKey, site, null);
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
        }
    }

    @Override
    public List<EntityContent<EntityKey>> getHtmlDescriptions(ArrayList<EntityKey> keys) {
        try {
            return renderer.render(keys, siteStore.getSites(keys), null);
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
        }
    }

    @Override
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
            throw new ServiceException(COULD_NOT_GUESS_SITE_NAME);
        }
        return guessName;
    }

    @Override
    public SiteUpdateResult update(SiteData siteData) {
        try {
            if (siteStore.contains(siteData.getName()) && siteData.getIsNew()) {
                return new SiteUpdateResult(State.entityExists, null);
            }
            EntityKey key = siteStore.update(SiteConverter.createFromSiteData(siteData));
            return new SiteUpdateResult(State.ok, key);
        } catch (Exception e) {
            LOG.error(COULD_NOT_UPDATE_SITE, e);
            throw new ServiceException(COULD_NOT_UPDATE_SITE);
        }
    }
}
