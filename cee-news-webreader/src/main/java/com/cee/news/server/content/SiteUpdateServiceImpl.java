package com.cee.news.server.content;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class SiteUpdateServiceImpl extends RemoteServiceServlet implements
		SiteUpdateService {

	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site";

	private static final long serialVersionUID = 8695157160684778713L;

	private static final Logger log = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private SiteStore siteStore;

	private final ThreadPoolExecutor pool;

	private final LinkedBlockingQueue<Runnable> workQueue;

	public SiteUpdateServiceImpl(int threadPoolSize) {
		workQueue = new LinkedBlockingQueue<Runnable>();
		pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0,
				TimeUnit.MILLISECONDS, workQueue);
	}
	
	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}

	@Override
	public int addSiteToUpdateQueue(String site) {
		if (!workQueue.contains(site)) {
			Site siteEntity = null;
			try {
				siteEntity = siteStore.getSite(site);				
			} catch (StoreException e) {
				log.error(COULD_NOT_RETRIEVE_SITE, e);
				throw new RuntimeException(COULD_NOT_RETRIEVE_SITE);
			}
			SiteUpdateCommand command = createUpdateSiteCommand();
			command.setSite(siteEntity);
			pool.execute(command);
		}
		return workQueue.size();
	}

	@Override
	public int addSitesToUpdateQueue(List<String> sites) {
		for (String site : sites) {
			addSiteToUpdateQueue(site);
		}
		return workQueue.size();
	}

	@Override
	public int getUpdateQueueSize() {
		return workQueue.size();
	}
	
	/**
	 * @return A update site command which all dependencies injected
	 */
	protected abstract SiteUpdateCommand createUpdateSiteCommand();
}