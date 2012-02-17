package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Provides access to the news
 */
@RemoteServiceRelativePath("services/gwtNewsService")
public interface NewsService extends RemoteService {

    List<EntityKey> getArticlesOfSite(String siteKey);
	
	List<EntityKey> getArticlesOfSites(List<String> siteKeys);
    
	List<EntityKey> getArticlesOfWorkingSet(String workingSetName);
	
	List<EntityKey> getRelatedArticles(String articleId, String workingSetName);
	
	List<EntityKey> findArticles(List<String> siteKeys, String searchQuery);
	
	EntityKey getHtmlDescription(EntityKey articleKey);
	
	List<EntityKey> getHtmlDescriptions(List<EntityKey> keys);
	
	EntityKey getHtmlContent(EntityKey articleKey);
	
	List<EntityKey> getHtmlContents(ArrayList<EntityKey> keys);
}
