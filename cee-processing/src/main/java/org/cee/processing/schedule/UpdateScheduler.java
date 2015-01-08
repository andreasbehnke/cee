package org.cee.processing.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.cee.parser.SiteReader;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.site.Site;
import org.cee.store.site.SiteStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class UpdateScheduler {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateScheduler.class);
	
	private static final String COULD_NOT_BE_REMOVED_FROM_LIST_OF_SITES_IN_PROGRESS = "{} could not be removed from list of sites in progress";

	private static final String STARTING_UPDATE_SCHEDULER_WITH_A_DELAY_OF = "Starting update scheduler with an initial delay of {} minutes and a periodic delay of {} minutes.";

	private static final String STARTING_SITE_UPDATES_ENCOUNTERED_AN_ERROR = "Starting site updates encontered an error";

	private static final String SCHEDULER_STARTING_SITE_UPDATES = "Scheduler starting site updates";
	
	private static final String SCHEDULER_QUEUED_SITES_FOR_UPDATE = "Scheduler queued {} sites for update";
	
	private static final String SITE_UPDATE_ENCOUNTERED_AN_ERROR = "Site update %s encountered an error";

	private static final String SITE_UPDATER_THREAD_PREFIX = "siteUpdater";

	private static final String UPDATE_SCHEDULER_THREAD_PREFIX = "updateScheduler";

	private long updateSchedulerInitialDelay;
	
	private long updateSchedulerFixedDelay;

	private int corePoolSize; 
	
	private int maxPoolSize;
	
	private long keepAliveTime;
	
	private List<EntityKey> sitesInProgress = new ArrayList<EntityKey>();

	private LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

	private ThreadPoolExecutor pool;
	
	private ScheduledExecutorService scheduler;
	
	private WebClientFactory webClientFactory;
	
	private SiteStore siteStore;
	
	private SiteReader siteReader;
	
	public void setWebClientFactory(WebClientFactory webClientFactory) {
		this.webClientFactory = webClientFactory;
	}
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
	
	public void setSiteReader(SiteReader siteReader) {
		this.siteReader = siteReader;
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
	
	public synchronized void start() {
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
	
	private WebClient createWebClient() {
		if (webClientFactory == null) {
			throw new IllegalStateException("Property webClientFactory has not been set");
		}
		return webClientFactory.createWebClient();
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
	
	public synchronized boolean addSiteToUpdateQueue(final EntityKey siteKey) throws StoreException {
        ensureThreadPool();
        if (!sitesInProgress.contains(siteKey)) {
            Site site = siteStore.getSite(siteKey);
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
}
