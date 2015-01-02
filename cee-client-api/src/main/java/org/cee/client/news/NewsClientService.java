package org.cee.client.news;

import java.util.ArrayList;
import java.util.List;

import org.cee.client.EntityContent;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;

public interface NewsClientService {

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
