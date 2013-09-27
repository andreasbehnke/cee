package com.cee.news.store;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;

/**
 * The site store is responsible for making sites persistent
 */
public interface SiteStore {
    
    /**
     * Creates or updates a site
     * @param site The site which needs update or should be added
     * @return The entity of the site being added
     */
    EntityKey update(Site site) throws StoreException;
    
    /**
     * Tests the existence of a site
     * @param name the name of the site to test
     * @return True, if a site with the given name exist
     */
    boolean contains(String name) throws StoreException;
    
    /**
     * Get a site by its unique name
     * @param key The site's key as returned by methods for retrieving site lists
     * @return Site or null if site not found
     */
    Site getSite(EntityKey key) throws StoreException;
    
    /**
     * Retrieves the sites references by the given keys
     * @param keys The sites primary keys
     * @return List of sites
     */
    List<Site> getSites(List<EntityKey> keys) throws StoreException;
    
    /**
     * @return List of site keys ordered by name
     */
    List<EntityKey> getSitesOrderedByName() throws StoreException;
}
