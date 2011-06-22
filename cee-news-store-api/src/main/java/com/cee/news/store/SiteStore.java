package com.cee.news.store;

import java.util.List;

import com.cee.news.model.Site;

/**
 * The site store is responsible for making sites persistent
 */
public interface SiteStore {
    
    /**
     * Creates or updates a site
     * @param site 
     */
    void update(Site site) throws StoreException;
    
    /**
     * Tests the existence of a site
     * @param name the name of the site to test
     * @return True, if a site with the given name exist
     */
    boolean contains(String name) throws StoreException;
    
    /**
     * Get a site by its unique name
     * @param name The site's name
     * @return Site or null if site not found
     */
    Site getSite(String name) throws StoreException;
    
    /**
     * @return List of URL referencing 
     */
    List<String> getSitesOrderedByName() throws StoreException;
}
