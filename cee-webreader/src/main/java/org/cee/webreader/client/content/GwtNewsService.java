package org.cee.webreader.client.content;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
public interface GwtNewsService extends RemoteService {

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
