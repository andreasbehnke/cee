package org.cee.webreader.server.content;

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

import org.cee.service.article.ArticleService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.article.ArticleKey;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.content.GwtNewsService;
import org.cee.webreader.client.error.ServiceException;
import org.cee.webreader.server.content.renderer.NewsContentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsServiceImpl implements GwtNewsService {

	private static final Logger LOG = LoggerFactory.getLogger(NewsServiceImpl.class);

	private static final String PARAMETER_KEYS_MUST_NOT_BE_NULL = "Parameter keys must not be null";

    private static final String PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL = "Parameter articleKey must not be null";

    private static final String COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST = "Could not retrieve contents for key list";

	private static final String COULD_NOT_RETRIEVE_CONTENT_FOR = "Could not retrieve content for %s";

	private static final String COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR = "Could not retrieve related articles for %s";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET = "Could not retrieve articles of working set %s";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITE = "Could not retrieve articles of site %s";
	
	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITES = "Could not retrieve articles of sites";
	
	private static final String COULD_NOT_FIND_ARTICLES_FOR_SEARCH = "Could not find articles for search %s";
    
    private ArticleService articleService;
	
	private NewsContentRenderer renderer = new NewsContentRenderer();
	
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	@Override
	public List<ArticleKey> getArticlesOfSite(EntityKey siteKey) {
	    try {
			return articleService.articlesOfSite(siteKey);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_SITE, siteKey);
			LOG.error(message , exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> getArticlesOfSites(List<EntityKey> siteKeys) {
	    try {
            return articleService.articlesOfSites(siteKeys);
        } catch (Exception exception) {
            LOG.error(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES , exception);
            throw new ServiceException(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES);
        }
	}
	
	@Override
	public List<ArticleKey> getArticlesOfWorkingSet(EntityKey workingSetKey) {
	    try {
			return articleService.articlesOfWorkingSet(workingSetKey);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET, workingSetKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> getRelatedArticles(ArticleKey articleKey, EntityKey workingSetKey) {
	    try {
			return articleService.relatedArticles(articleKey, workingSetKey);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> findArticles(List<EntityKey> siteKeys, EntityKey workingSetKey, String searchQuery) {
	    try {
	    	return articleService.findArticles(siteKeys, workingSetKey, searchQuery);
        } catch (Exception exception) {
            String message = String.format(COULD_NOT_FIND_ARTICLES_FOR_SEARCH, searchQuery);
            LOG.error(message, exception);
            throw new ServiceException(message);
        }
	}
	
	protected EntityContent<ArticleKey> render(ArticleKey key, String template) throws StoreException {
	    return renderer.render(
                key, 
                articleService.get(key), 
                template);
	}
	
	protected List<EntityContent<ArticleKey>> render(List<ArticleKey> keys, String template) throws StoreException {
        return renderer.render(
                keys, 
                articleService.get(keys), 
                template);
    }
	
	@Override
	public EntityContent<ArticleKey> getHtmlDescription(ArticleKey articleKey) {
	    if (articleKey == null) {
            throw new IllegalArgumentException(PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL);
        }
	    try {
	        return render(articleKey, NewsContentRenderer.DESCRIPTION_TEMPLATE);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_CONTENT_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityContent<ArticleKey>> getHtmlDescriptions(List<ArticleKey> keys) {
	    if (keys == null) {
            throw new IllegalArgumentException(PARAMETER_KEYS_MUST_NOT_BE_NULL);
        }
	    try {
	        return render(keys, NewsContentRenderer.DESCRIPTION_TEMPLATE);
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
	
	@Override
	public EntityContent<ArticleKey> getHtmlContent(ArticleKey articleKey) {
	    if (articleKey == null) {
            throw new IllegalArgumentException(PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL);
        }
	    try {
	        return render(articleKey, NewsContentRenderer.CONTENT_TEMPLATE);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_CONTENT_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityContent<ArticleKey>> getHtmlContents(ArrayList<ArticleKey> keys) {
	    if (keys == null) {
            throw new IllegalArgumentException(PARAMETER_KEYS_MUST_NOT_BE_NULL);
        }
	    try {
	        return render(keys, NewsContentRenderer.CONTENT_TEMPLATE);
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
}