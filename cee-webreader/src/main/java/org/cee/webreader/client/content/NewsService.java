package org.cee.webreader.client.content;

import java.util.ArrayList;
import java.util.List;

import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Provides access to the news
 */
@RemoteServiceRelativePath("services/gwtNewsService")
public interface NewsService extends RemoteService {

    List<ArticleKey> getArticlesOfSite(EntityKey siteKey);
	
	List<ArticleKey> getArticlesOfSites(List<EntityKey> siteKeys);
    
	List<ArticleKey> getArticlesOfWorkingSet(EntityKey workingSetKey);
	
	List<ArticleKey> getRelatedArticles(ArticleKey articleKey, EntityKey workingSetKey);
	
	List<ArticleKey> findArticles(List<EntityKey> siteKeys, EntityKey workingSetKey, String searchQuery);
	
	EntityContent<ArticleKey> getHtmlDescription(ArticleKey articleKey);
	
	List<EntityContent<ArticleKey>> getHtmlDescriptions(List<ArticleKey> keys);
	
	EntityContent<ArticleKey>  getHtmlContent(ArticleKey articleKey);
	
	List<EntityContent<ArticleKey>> getHtmlContents(ArrayList<ArticleKey> keys);
}
