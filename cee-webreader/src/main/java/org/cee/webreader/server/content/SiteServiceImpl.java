package org.cee.webreader.server.content;

/*
 * #%L
 * News Reader
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


import java.util.ArrayList;
import java.util.List;

import org.cee.client.EntityContent;
import org.cee.client.site.SiteData;
import org.cee.client.site.SiteUpdateResult;
import org.cee.service.site.SiteService;
import org.cee.store.EntityKey;
import org.cee.store.site.Site;
import org.cee.webreader.client.content.GwtSiteService;
import org.cee.webreader.client.error.ServiceException;
import org.cee.webreader.server.content.renderer.SiteContentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteServiceImpl implements GwtSiteService {

    private static final Logger LOG = LoggerFactory.getLogger(SiteServiceImpl.class);

    private static final String COULD_NOT_RETRIEVE_SITE_DESCRIPTION = "Could not retrieve site description";

    private static final String COULD_NOT_RETRIEVE_SITE_LIST = "Could not retrieve site list";

    private static final String COULD_NOT_UPDATE_SITE = "Could not update site";

    private SiteService siteService;

    private SiteContentRenderer renderer = new SiteContentRenderer();

    public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
    
    @Override
    public List<EntityKey> getSites() {
        try {
            return siteService.orderedByName();
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
        }
    }

    @Override
    public List<EntityKey> getSitesOfWorkingSet(EntityKey workingSetKey) {
        try {
        	return siteService.sitesOfWorkingSet(workingSetKey);
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_LIST, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_LIST);
        }
    }

    @Override
    public EntityContent<EntityKey> getHtmlDescription(EntityKey siteKey) {
        try {
            SiteData site = siteService.get(siteKey);
            return renderer.render(siteKey, site, null);
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
        }
    }

    @Override
    public List<EntityContent<EntityKey>> getHtmlDescriptions(ArrayList<EntityKey> keys) {
        try {
            return renderer.render(keys, siteService.get(keys), null);
        } catch (Exception e) {
            LOG.error(COULD_NOT_RETRIEVE_SITE_DESCRIPTION, e);
            throw new ServiceException(COULD_NOT_RETRIEVE_SITE_DESCRIPTION);
        }
    }

    @Override
    public String guessUniqueSiteName(String name) {
        return siteService.guessUniqueSiteName(name);
    }

    @Override
    public SiteUpdateResult update(SiteData siteData) {
        try {
            return siteService.update(siteData);
        } catch (Exception e) {
            LOG.error(COULD_NOT_UPDATE_SITE, e);
            throw new ServiceException(COULD_NOT_UPDATE_SITE);
        }
    }
}
