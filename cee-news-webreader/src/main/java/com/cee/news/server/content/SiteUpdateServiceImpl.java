package com.cee.news.server.content;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.cee.news.client.content.FeedData;
import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteData.SiteRetrivalState;
import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.cee.news.model.Site;
import com.cee.news.parser.FeedParser;
import com.cee.news.parser.SiteParser;
import com.cee.news.store.SiteStore;

public abstract class SiteUpdateServiceImpl implements SiteUpdateService {

	private static final Logger LOG = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private static final String STARTING_SITE_UPDATES_ENCOUNTERED_AN_ERROR = "Starting site updates encontered an error";

	private static final String SCHEDULER_STARTING_SITE_UPDATES = "Scheduler starting site updates";

	private static final String STARTING_UPDATE_SCHEDULER_WITH_A_DELAY_OF = "Starting update scheduler with an initial delay of {} minutes and a periodic delay of {} minutes.";

	private static final String CLEARING_WORK_QUEUE = "Clearing work queue";

	private static final String COULD_NOT_BE_REMOVED_FROM_LIST_OF_RUNNABLES = "{} could not be removed from list of runnables";

	private static final String COULD_NOT_BE_REMOVED_FROM_LIST_OF_SITES_IN_PROGRESS = "{} could not be removed from list of sites in progress";

	private static final String SITE_UPDATE_ENCOUNTERED_AN_ERROR = "Site update %s encountered an error";

	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site: %s";

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

	private ScheduledExecutorService scheduler;

	private List<Runnable> runnablesInProgress = new ArrayList<Runnable>();

	private LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
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
			pool = new ThreadPoolExecutor(
					corePoolSize, 
					maxPoolSize, 
					keepAliveTime,
					TimeUnit.MILLISECONDS, 
					workQueue) {
				
					@Override
					protected void afterExecute(Runnable r, Throwable t) {
						super.afterExecute(r, t);
						SiteUpdateServiceImpl.this.removeRunnable(r);
					}
			};
			pool.setThreadFactory( new PrefixCountThreadFactory(SITE_UPDATER_THREAD_PREFIX) );
		}
	}

	private synchronized void removeSite(EntityKey siteKey) {
		if (!sitesInProgress.remove(siteKey)) {
			LOG.warn(COULD_NOT_BE_REMOVED_FROM_LIST_OF_SITES_IN_PROGRESS, siteKey);
		}
	}
	
	private synchronized void removeRunnable(Runnable runnable) {
		if (!runnablesInProgress.remove(runnable)) {
			LOG.warn(COULD_NOT_BE_REMOVED_FROM_LIST_OF_RUNNABLES, runnable);
		}
	}

	@Override
	public synchronized void clearQueue() {
		LOG.info(CLEARING_WORK_QUEUE);
		workQueue.clear();
		runnablesInProgress.clear();
		sitesInProgress.clear();
	}

	@Override
	public SiteData retrieveSiteData(String location) {
		SiteParser parser = createSiteParser();
		SiteData info = new SiteData();
		info.setIsNew(true);
		URL locationUrl = null;
		try {
			locationUrl = new URL(location);
		} catch (MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			return info;
		}
		try {
			Site site = parser.parse(locationUrl);
			info = SiteConverter.createFromSite(site);
			info.setState(SiteRetrivalState.ok);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.error(String.format(COULD_NOT_RETRIEVE_SITE, location), e);
		} catch (SAXException e) {
			info.setState(SiteRetrivalState.parserError);
			LOG.error(String.format(COULD_NOT_RETRIEVE_SITE, location), e);
		} catch (Exception e) {
			String message = String.format(COULD_NOT_RETRIEVE_SITE, location);
			LOG.error(message, e);
			throw new ServiceException(message);
		}
		return info;
	}
	
	@Override
	public FeedData retrieveFeedData(String location) {
		FeedParser feedParser = createFeedParser();
		FeedData info = new FeedData();
		info.setIsNew(true);
		URL locationUrl = null;
		try {
			locationUrl = new URL(location);
		} catch (MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			return info;
		}
		try {
			if (!feedParser.isSupportedFeed(locationUrl)) {
				info.setState(SiteRetrivalState.parserError);
				LOG.error(String.format(COULD_NOT_RETRIEVE_SITE, location));
				return info;
			}
			Feed feed = feedParser.parse(locationUrl);
			info = SiteConverter.createFromFeed(feed);
			info.setState(SiteRetrivalState.ok);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.error(String.format(COULD_NOT_RETRIEVE_SITE, location), e);
		}
		return info;
	}
	
	@Override
	public synchronized void startUpdateScheduler() {
		if (scheduler == null) {
			LOG.info(STARTING_UPDATE_SCHEDULER_WITH_A_DELAY_OF, updateSchedulerInitialDelay, updateSchedulerFixedDelay);
			scheduler = new ScheduledThreadPoolExecutor(1, new PrefixCountThreadFactory(UPDATE_SCHEDULER_THREAD_PREFIX));
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
	
	public synchronized void addSiteToUpdateQueue(final EntityKey siteKey) {
        ensureThreadPool();
        if (!sitesInProgress.contains(siteKey)) {
            Site siteEntity = null;
            try {
                siteEntity = siteStore.getSite(siteKey);
            } catch (Exception e) {
                String message = String.format(COULD_NOT_RETRIEVE_SITE, siteKey);
                LOG.error(message, e);
                throw new ServiceException(message);
            }
            SiteUpdateCommand command = createSiteUpdateCommand();
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
            command.setSite(siteEntity);
            sitesInProgress.add(siteKey);
            runnablesInProgress.add(command);
            pool.execute(command);
        }
    }

	private void startSiteUpdates() {
		try {
			LOG.info(SCHEDULER_STARTING_SITE_UPDATES);
			List<EntityKey> sites = siteStore.getSitesOrderedByName();
			for (EntityKey entityKey : sites) {
				addSiteToUpdateQueue(entityKey);
			}
		} catch (Throwable t) {
			LOG.error(STARTING_SITE_UPDATES_ENCOUNTERED_AN_ERROR, t);
		}
	}

	/**
	 * Stops execution of the thread pool
	 */
	public synchronized void shutdown() {
		if (pool != null) {
			pool.shutdownNow();
		}
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
	}

	/**
	 * @return A update site command prepared with all dependencies
	 */
	protected abstract SiteUpdateCommand createSiteUpdateCommand();

	/**
	 * @return A site parser prepared with all dependencies
	 */
	protected abstract SiteParser createSiteParser();
	
	/**
	 * @return A feed parser instance prepared with all dependencies
	 */
	protected abstract FeedParser createFeedParser();
}