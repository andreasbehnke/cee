package org.cee.service.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.cee.client.site.FeedData;
import org.cee.client.site.SiteConverter;
import org.cee.client.site.SiteData;
import org.cee.service.DuplicateKeyException;
import org.cee.service.EntityNotFoundException;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.site.Site;
import org.cee.store.site.SiteStore;
import org.cee.store.workingset.WorkingSet;
import org.cee.store.workingset.WorkingSetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);

    private static final String PREFIX_HTTP_WWW = "http://www.";

    private static final String COULD_NOT_GUESS_SITE_NAME = "Could not guess site name";

	private SiteStore siteStore;

    private WorkingSetStore workingSetStore;
    
    private Validator validator;

    public void setSiteStore(SiteStore siteStore) {
        this.siteStore = siteStore;
    }

    public void setWorkingSetStore(WorkingSetStore workingSetStore) {
        this.workingSetStore = workingSetStore;
    }
    
    public void setValidator(Validator validator) {
		this.validator = validator;
	}

    public List<EntityKey> orderedByName() throws StoreException {
    	return siteStore.getSitesOrderedByName();
    }

    public List<EntityKey> sitesOfWorkingSet(EntityKey workingSetKey) throws StoreException, EntityNotFoundException {
    	WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetKey);
    	if (workingSet == null) {
    		throw new EntityNotFoundException(workingSetKey);
    	}
        return workingSet.getSites();
    }
    
    public SiteData get(EntityKey siteKey) throws StoreException, EntityNotFoundException {
    	Site site = siteStore.getSite(siteKey);
    	if (site == null) {
    		throw new EntityNotFoundException(siteKey);
    	}
    	return SiteConverter.createFromSite(site, false);
    }
    
    public List<SiteData> get(List<EntityKey> siteKeys) throws StoreException {
    	List<SiteData> siteDatas = new ArrayList<>();
    	List<Site> sites = siteStore.getSites(siteKeys);
    	for (Site site : sites) {
    		siteDatas.add(SiteConverter.createFromSite(site, false));
    	}
    	return siteDatas;
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

    public SiteData update(SiteData siteData) throws StoreException, DuplicateKeyException {
    	Set<ConstraintViolation<SiteData>> issues = validator.validate(siteData);
    	if (!issues.isEmpty()) {
    		throw new ConstraintViolationException(issues);
    	}
    	if (siteData.getIsNew() && siteStore.contains(siteData.getName())) {
        	throw new DuplicateKeyException(EntityKey.get(siteData.getName()));
        }
    	EntityKey language = siteData.getLanguage();
        for (FeedData feedData : siteData.getFeeds()) {
            if (feedData.getLanguage() == null) {
            	//if feed does not provide language information,
            	//set language to site's language
            	feedData.setLanguage(language);
            }
        }
        siteStore.update(SiteConverter.createFromSiteData(siteData));
        return siteData;
    }
}
