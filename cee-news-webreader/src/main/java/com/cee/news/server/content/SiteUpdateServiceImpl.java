package com.cee.news.server.content;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.cee.news.model.NamedKey;
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

	private static final Logger log = LoggerFactory
			.getLogger(SiteUpdateServiceImpl.class);

	private SiteStore siteStore;

	private WorkingSetStore workingSetStore;

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

	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}

	@Override
	public int addSiteToUpdateQueue(String siteName) {
		if (!workQueue.contains(siteName)) {
			Site siteEntity = null;
			try {
				siteEntity = siteStore.getSite(siteName);
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
	public int addSitesOfWorkingSetToUpdateQueue(String workingSetName) {
		try {
			WorkingSet ws = workingSetStore.getWorkingSet(workingSetName);
			if (ws == null)
				throw new IllegalArgumentException("Unknown working set: "
						+ workingSetName);
			for (NamedKey siteKey : ws.getSites()) {
				addSiteToUpdateQueue(siteKey.getName());
			}
			return workQueue.size();
		} catch (StoreException se) {
			throw new ServiceException(se.toString());
		}
	}

	@Override
	public int getUpdateQueueSize() {
		return workQueue.size();
	}

	@Override
	public void clearQueue() {
		workQueue.clear();
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
	protected abstract SiteUpdateCommand createUpdateSiteCommand();

	/**
	 * @return A site parser prepared with all dependencies
	 */
	protected abstract SiteParser createSiteParser();
}