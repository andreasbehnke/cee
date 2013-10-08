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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.news.store.SiteStore;
import org.cee.parser.ParserException;
import org.cee.parser.SiteReader;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;
import org.cee.webreader.client.content.FeedData;
import org.cee.webreader.client.content.SiteData;
import org.cee.webreader.client.content.SiteUpdateService;
import org.cee.webreader.client.content.SiteData.SiteRetrivalState;
import org.cee.webreader.client.error.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteUpdateServiceImpl implements SiteUpdateService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private static final String STARTING_SITE_UPDATES_ENCOUNTERED_AN_ERROR = "Starting site updates encontered an error";

	private static final String SCHEDULER_STARTING_SITE_UPDATES = "Scheduler starting site updates";
	
	private static final String SCHEDULER_QUEUED_SITES_FOR_UPDATE = "Scheduler queued {} sites for update";

	private static final String STARTING_UPDATE_SCHEDULER_WITH_A_DELAY_OF = "Starting update scheduler with an initial delay of {} minutes and a periodic delay of {} minutes.";

	private static final String COULD_NOT_BE_REMOVED_FROM_LIST_OF_SITES_IN_PROGRESS = "{} could not be removed from list of sites in progress";

	private static final String SITE_UPDATE_ENCOUNTERED_AN_ERROR = "Site update %s encountered an error";

	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site: %s";
	
	private static final String MALFORMED_URL = "The URL {} is malformed";
	
	private static final String IO_ERROR = "Resource {} could not be loaded, an IO error occured.";
	
	private static final String PARSER_ERROR = "Resource {} could not be loaded, a parser error occured";

	private static final String UPDATE_SCHEDULER_THREAD_PREFIX = "updateScheduler";

	private static final String SITE_UPDATER_THREAD_PREFIX = "siteUpdater";

	private int corePoolSize; 
	
	private int maxPoolSize;
	
	private long keepAliveTime;

	private long updateSchedulerInitialDelay;
	
	private long updateSchedulerFixedDelay;
	
	private ThreadPoolExecutor pool;
	
	private List<EntityKey> sitesInProgress = new ArrayList<EntityKey>();

	private SiteStore siteStore;
	
	private SiteReader siteReader;
	
	private WebClientFactory webClientFactory;

	private ScheduledExecutorService scheduler;

	private LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
	
	public void setSiteReader(SiteReader siteReader) {
	    this.siteReader = siteReader;
    }
	
	public void setWebClientFactory(WebClientFactory webClientFactory) {
	    this.webClientFactory = webClientFactory;
    }
	
	/**
	 * @return the minimum thread pool size
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	/**
	 * @return the maximum thread pool size
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return time in milliseconds an unused thread is kept alive if the pool size exceeds the core pool size
	 */
	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	/**
	 * @return initial delay in minutes for the first update scheduler run
	 */
	public long getUpdateSchedulerInitialDelay() {
        return updateSchedulerInitialDelay;
    }

    public void setUpdateSchedulerInitialDelay(long updateSchedulerInitialDelay) {
        this.updateSchedulerInitialDelay = updateSchedulerInitialDelay;
    }

    /**
	 * @return delay in minutes between update scheduler runs
	 */
	public long getUpdateSchedulerFixedDelay() {
		return updateSchedulerFixedDelay;
	}

	public void setUpdateSchedulerFixedDelay(long updateSchedulerFixedDelay) {
		this.updateSchedulerFixedDelay = updateSchedulerFixedDelay;
	}
	
	private synchronized void ensureThreadPool() {
		if (pool == null) {
			pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
			pool.setThreadFactory( new PrefixCountThreadFactory(SITE_UPDATER_THREAD_PREFIX, true) );
		}
	}

	private synchronized void removeSite(EntityKey siteKey) {
		if (!sitesInProgress.remove(siteKey)) {
			LOG.warn(COULD_NOT_BE_REMOVED_FROM_LIST_OF_SITES_IN_PROGRESS, siteKey);
		}
	}
	

	private WebClient createWebClient() {
		if (webClientFactory == null) {
			throw new IllegalStateException("Property webClientFactory has not been set");
		}
		return webClientFactory.createWebClient();
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
			String message = String.format(COULD_NOT_RETRIEVE_SITE, location);
			LOG.error(message, e);
			throw new ServiceException(message);
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
			String message = String.format(COULD_NOT_RETRIEVE_SITE, location);
			LOG.error(message, e);
			throw new ServiceException(message);
		}
		return info;
	}
	
	@Override
	public synchronized void startUpdateScheduler() {
		if (scheduler == null) {
			LOG.info(STARTING_UPDATE_SCHEDULER_WITH_A_DELAY_OF, updateSchedulerInitialDelay, updateSchedulerFixedDelay);
			scheduler = new ScheduledThreadPoolExecutor(1, new PrefixCountThreadFactory(UPDATE_SCHEDULER_THREAD_PREFIX, true));
			scheduler.scheduleWithFixedDelay(
					new Runnable() {		
						@Override
						public void run() {
							startSiteUpdates();
						}
					}, 
					updateSchedulerInitialDelay, 
					updateSchedulerFixedDelay,
					TimeUnit.MINUTES);
		}
	}
	
	@Override
	public synchronized boolean addSiteToUpdateQueue(final EntityKey siteKey) {
        ensureThreadPool();
        if (!sitesInProgress.contains(siteKey)) {
            Site site = null;
            try {
                site = siteStore.getSite(siteKey);
            } catch (Exception e) {
                String message = String.format(COULD_NOT_RETRIEVE_SITE, siteKey);
                LOG.error(message, e);
                throw new ServiceException(message);
            }
            SiteUpdateCommand command = new SiteUpdateCommand(createWebClient(), siteReader, site);
            command.addCommandCallback(new CommandCallback() {
                @Override
                public void notifyFinished() {
                    removeSite(siteKey);
                }
                @Override
                public void notifyError(Exception ex) {
                	String message = String.format(SITE_UPDATE_ENCOUNTERED_AN_ERROR, siteKey);
                    LOG.error(message, ex);
                }
            });
            sitesInProgress.add(siteKey);
            pool.execute(command);
            return true;
        }
        return false;
    }

	private void startSiteUpdates() {
		try {
			LOG.info(SCHEDULER_STARTING_SITE_UPDATES);
			List<EntityKey> sites = siteStore.getSitesOrderedByName();
			int siteCount = 0;
			for (EntityKey entityKey : sites) {
				if (addSiteToUpdateQueue(entityKey)) {
					siteCount++;
				}
			}
			LOG.info(SCHEDULER_QUEUED_SITES_FOR_UPDATE, siteCount);
		} catch (Throwable t) {
			LOG.error(STARTING_SITE_UPDATES_ENCOUNTERED_AN_ERROR, t);
		}
	}
}