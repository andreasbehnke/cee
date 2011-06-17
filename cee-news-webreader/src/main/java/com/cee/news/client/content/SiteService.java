package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("services/gwtSiteService")
public interface SiteService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static SiteServiceAsync instance;
		public static SiteServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SiteService.class);
			}
			return instance;
		}
	}
	
	List<String> getSites();
	
	SafeHtml getTitle(String name);
	
	SafeHtml getHtmlDescription(String name);
}
