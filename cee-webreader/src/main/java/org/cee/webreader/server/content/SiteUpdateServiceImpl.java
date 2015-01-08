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

import org.cee.client.site.FeedData;
import org.cee.client.site.SiteData;
import org.cee.processing.schedule.UpdateScheduler;
import org.cee.processing.site.SiteProcessor;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.webreader.client.content.GwtSiteUpdateService;
import org.cee.webreader.client.error.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteUpdateServiceImpl implements GwtSiteUpdateService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site: %s";
		
	private UpdateScheduler updateScheduler;
	
	private SiteProcessor siteProcessor;
	
	public void setUpdateScheduler(UpdateScheduler updateScheduler) {
		this.updateScheduler = updateScheduler;
	}
	
	public void setSiteProcessor(SiteProcessor siteProcessor) {
		this.siteProcessor = siteProcessor;
	}
		
	private ServiceException createCouldNotRetrieveSiteException(Exception e, Object location) {
		String message = String.format(COULD_NOT_RETRIEVE_SITE, location);
		LOG.error(message, e);
		return new ServiceException(message);
	}

	@Override
	public SiteData retrieveSiteData(String location) {
		try {
			return siteProcessor.retrieveSiteData(location);
		} catch (Exception e) {
			throw createCouldNotRetrieveSiteException(e, location);
		}
	}
	
	@Override
	public FeedData retrieveFeedData(String location) {
		try {
			return siteProcessor.retrieveFeedData(location);
		} catch (Exception e) {
			throw createCouldNotRetrieveSiteException(e, location);
		}
	}
	
	@Override
	public void startUpdateScheduler() {
		updateScheduler.start();
	}
	
	@Override
	public boolean addSiteToUpdateQueue(final EntityKey siteKey) {
        try {
			return updateScheduler.addSiteToUpdateQueue(siteKey);
		} catch (StoreException e) {
			throw createCouldNotRetrieveSiteException(e, siteKey);
		}
    }
}