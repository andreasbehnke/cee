package com.cee.news.server.content;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.cee.news.client.content.SiteData;
import com.cee.news.client.content.SiteData.SiteRetrivalState;
import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.model.WorkingSet;
import com.cee.news.parser.SiteParser;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class SiteUpdateServiceImpl extends RemoteServiceServlet
		implements SiteUpdateService {

	private static final String COULD_NOT_RETRIEVE_SITE = "Could not retrieve site";

	private static final long serialVersionUID = 8695157160684778713L;

	private static final Logger log = LoggerFactory.getLogger(SiteUpdateServiceImpl.class);
	
	private final List<String> sitesInProgress;

	private SiteStore siteStore;

	private WorkingSetStore workingSetStore;

	private final ThreadPoolExecutor pool;

	private final LinkedBlockingQueue<Runnable> workQueue;

	public SiteUpdateServiceImpl(int threadPoolSize) {
		workQueue = new LinkedBlockingQueue<Runnable>();
		pool = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0,
				TimeUnit.MILLISECONDS, workQueue);
		sitesInProgress = new ArrayList<String>();
	}

	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}

	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}
	
	private synchronized void removeSite(String siteName) {
		if (!sitesInProgress.remove(siteName)) {
			log.warn("{} could not be removed from list of sites in progress", siteName);
		}
	}

	@Override
	public synchronized int addSiteToUpdateQueue(final String siteName) {
		if (!sitesInProgress.contains(siteName)) {
			Site siteEntity = null;
			try {
				siteEntity = siteStore.getSite(siteName);
			} catch (StoreException e) {
				log.error(COULD_NOT_RETRIEVE_SITE, e);
				throw new ServiceException(COULD_NOT_RETRIEVE_SITE);
			}
			SiteUpdateCommand command = createSiteUpdateCommand();
			command.addCommandCallback(new CommandCallback() {
				
				@Override
				public void notifyFinished() {
					removeSite(siteName);
				}
				
				@Override
				public void notifyError(Exception ex) {
					//TODO: how to handle error reporting for the user?
				}
			});
			command.setSite(siteEntity);
			sitesInProgress.add(siteName);
			pool.execute(command);
		}
		return sitesInProgress.size();
	}

	@Override
	public synchronized int addSitesOfWorkingSetToUpdateQueue(String workingSetName) {
		try {
			WorkingSet ws = workingSetStore.getWorkingSet(workingSetName);
			if (ws == null)
				throw new IllegalArgumentException("Unknown working set: "
						+ workingSetName);
			for (EntityKey siteKey : ws.getSites()) {
				addSiteToUpdateQueue(siteKey.getName());
			}
			return sitesInProgress.size();
		} catch (StoreException se) {
			throw new ServiceException(se.toString());
		}
	}

	@Override
	public int getUpdateQueueSize() {
		return sitesInProgress.size();
	}

	@Override
	public synchronized void clearQueue() {
		log.info("Clearing work queue");
		workQueue.clear();
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
		} catch (SAXException e) {
			info.setState(SiteRetrivalState.parserError);
		}
		return info;
	}

	/**
	 * @return A update site command prepared with all dependencies
	 */
	protected abstract SiteUpdateCommand createSiteUpdateCommand();

	/**
	 * @return A site parser prepared with all dependencies
	 */
	protected abstract SiteParser createSiteParser();
}