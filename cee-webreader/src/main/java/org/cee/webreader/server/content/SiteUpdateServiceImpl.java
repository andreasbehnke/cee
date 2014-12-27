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

import java.io.IOException;
import java.net.MalformedURLException;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.news.store.StoreException;
import org.cee.parser.ParserException;
import org.cee.parser.SiteReader;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;
import org.cee.processing.UpdateScheduler;
import org.cee.webreader.client.content.FeedData;
import org.cee.webreader.client.content.SiteData;
import org.cee.webreader.client.content.SiteData.SiteRetrivalState;
import org.cee.webreader.client.content.SiteUpdateService;
import org.cee.webreader.client.error.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteUpdateServiceImpl implements SiteUpdateService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site: %s";
	
	private static final String MALFORMED_URL = "The URL {} is malformed";
	
	private static final String IO_ERROR = "Resource {} could not be loaded, an IO error occured.";
	
	private static final String PARSER_ERROR = "Resource {} could not be loaded, a parser error occured";
	
	private WebClientFactory webClientFactory;
	
	private SiteReader siteReader;
	
	private UpdateScheduler updateScheduler;
	
	public void setSiteReader(SiteReader siteReader) {
	    this.siteReader = siteReader;
    }
	
	public void setUpdateScheduler(UpdateScheduler updateScheduler) {
		this.updateScheduler = updateScheduler;
	}
	
	public void setWebClientFactory(WebClientFactory webClientFactory) {
	    this.webClientFactory = webClientFactory;
    }

	private WebClient createWebClient() {
		if (webClientFactory == null) {
			throw new IllegalStateException("Property webClientFactory has not been set");
		}
		return webClientFactory.createWebClient();
	}
	
	private ServiceException createCouldNotRetrieveSiteException(Exception e, Object location) {
		String message = String.format(COULD_NOT_RETRIEVE_SITE, location);
		LOG.error(message, e);
		return new ServiceException(message);
	}

	@Override
	public SiteData retrieveSiteData(String location) {
		SiteData info = new SiteData();
		info.setIsNew(true);
		try {
			Site site = siteReader.readSite(createWebClient(), location);
			info = SiteConverter.createFromSite(site);
			info.setState(SiteRetrivalState.ok);
		} catch(MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			LOG.warn(MALFORMED_URL, location);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.warn(IO_ERROR, location);
		} catch (ParserException e) {
			info.setState(SiteRetrivalState.parserError);
			LOG.warn(PARSER_ERROR, location);
		} catch (Exception e) {
			throw createCouldNotRetrieveSiteException(e, location);
		}
		return info;
	}
	
	@Override
	public FeedData retrieveFeedData(String location) {
		FeedData info = new FeedData();
		info.setIsNew(true);
		try {
			Feed feed = siteReader.readFeed(createWebClient(), location);
			info = SiteConverter.createFromFeed(feed);
			info.setState(SiteRetrivalState.ok);
		} catch(MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			LOG.warn(MALFORMED_URL, location);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.warn(IO_ERROR, location);
		} catch (ParserException e) {
			info.setState(SiteRetrivalState.parserError);
			LOG.warn(PARSER_ERROR, location);
		} catch (Exception e) {
			throw createCouldNotRetrieveSiteException(e, location);
		}
		return info;
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