package org.cee.store.site;

/*
 * #%L
 * Content Extraction Engine - News Store API
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.List;

import org.cee.store.EntityKey;
import org.cee.store.StoreException;

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
