package com.cee.news.client.content;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Provides access to the news
 */
@RemoteServiceRelativePath("services/gwtNewsService")
public interface NewsService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static NewsServiceAsync instance;
		public static NewsServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(NewsService.class);
			}
			return instance;
		}
	}
	
	List<String> getHeadlines(String siteUrl);
	
	SafeHtml getHtmlDescription(String siteUrl, int index);
	
	SafeHtml getHtmlContent(String siteUrl, int index);
}
