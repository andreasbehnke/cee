package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtSiteUpdateService")
public interface SiteUpdateService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SiteUpdateServiceAsync instance;
		public static SiteUpdateServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SiteUpdateService.class);
			}
			return instance;
		}
	}
	
	int updateSites(List<String> sites);
	
	int getUpdateQueueSize();
}
