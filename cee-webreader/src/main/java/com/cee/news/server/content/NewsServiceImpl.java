package com.cee.news.server.content;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.NewsService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.cee.news.model.WorkingSet;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.server.content.renderer.NewsContentRenderer;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class NewsServiceImpl implements NewsService {

	private static final Logger LOG = LoggerFactory.getLogger(NewsServiceImpl.class);

	private static final String PARAMETER_KEYS_MUST_NOT_BE_NULL = "Parameter keys must not be null";

    private static final String PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL = "Parameter articleKey must not be null";

    private static final String PARAMETER_SEARCH_QUERY_MUST_NOT_BE_NULL = "Parameter searchQuery must not be null";

    private static final String PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL = "Parameter workingSetKey must not be null";

    private static final String PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL = "Parameter siteKeys must not be null";

    private static final String PARAMETER_SITE_KEY_MUST_NOT_BE_NULL = "Parameter siteKey must not be null";

    private static final String COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST = "Could not retrieve contents for key list";

	private static final String COULD_NOT_RETRIEVE_CONTENT_FOR = "Could not retrieve content for %s";

	private static final String COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR = "Could not retrieve related articles for %s";

	private static final String RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE = "Retrieved {} related articles for article {}";

	private static final String WORKING_SET_NOT_FOUND = "Working set %s not found";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET = "Could not retrieve articles of working set %s";

	private static final String RETRIEVED_ARTICLES_FOR_WORKING_SET = "Retrieved %s articles for working set %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITE = "Retrieved %s articles for site %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITES = "Retrieved {} articles for {} sites";
	
	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITE = "Could not retrieve articles of site %s";
	
	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITES = "Could not retrieve articles of sites";
	
	private static final String COULD_NOT_FIND_ARTICLES_FOR_SEARCH = "Could not find articles for search %s";
    
    private ArticleStore articleStore;

	private ArticleSearchService articleSearchService;
	
	private WorkingSetStore workingSetStore;
	
	private NewsContentRenderer renderer = new NewsContentRenderer();
	
	public void setArticleStore(ArticleStore articleStore) {
		this.articleStore = articleStore;
	}
	
	public void setArticleSearchService(ArticleSearchService articleSearchService) {
		this.articleSearchService = articleSearchService;
	}

	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}
	
	@Override
	public List<ArticleKey> getArticlesOfSite(EntityKey siteKey) {
	    if (siteKey == null) {
	        throw new IllegalArgumentException(PARAMETER_SITE_KEY_MUST_NOT_BE_NULL);
	    }
		try {
			List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(siteKey);
			LOG.debug(RETRIEVED_ARTICLES_FOR_SITE, keys.size(), siteKey);
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_SITE, siteKey);
			LOG.error(message , exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> getArticlesOfSites(List<EntityKey> siteKeys) {
	    if (siteKeys == null) {
            throw new IllegalArgumentException(PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL);
        }
	    try {
            List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(siteKeys);
            LOG.debug(RETRIEVED_ARTICLES_FOR_SITES, keys.size(), siteKeys.size());
            return keys;
        } catch (Exception exception) {
            LOG.error(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES , exception);
            throw new ServiceException(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES);
        }
	}
	
	@Override
	public List<ArticleKey> getArticlesOfWorkingSet(EntityKey workingSetKey) {
	    if (workingSetKey == null) {
            throw new IllegalArgumentException(PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL);
        }
		try {
			WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetKey);
			List<ArticleKey> keys = articleStore.getArticlesOrderedByDate(workingSet);
			LOG.debug(RETRIEVED_ARTICLES_FOR_WORKING_SET, keys.size(), workingSetKey);
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET, workingSetKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> getRelatedArticles(ArticleKey articleKey, EntityKey workingSetKey) {
	    if (articleKey == null) {
            throw new IllegalArgumentException(PARAMETER_ARTICLE_KEY_MUST_NOT_BE_NULL);
        }
	    if (workingSetKey == null) {
            throw new IllegalArgumentException(PARAMETER_WORKING_SET_KEY_MUST_NOT_BE_NULL);
        }
        try {
			WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
			if (ws == null) {
				throw new IllegalArgumentException(String.format(WORKING_SET_NOT_FOUND, workingSetKey));
			}
			List<EntityKey> sites = ws.getSites();
			List<EntityKey> relatedSites = null;
			
			if (sites.size() == 1) {
				relatedSites = sites;
			} else {
				//remove site of current article
				relatedSites = new ArrayList<EntityKey>(sites);
				String siteOfArticle = articleKey.getSiteKey();
				relatedSites.remove(EntityKey.get(siteOfArticle));
			}
			List<ArticleKey> keys = articleSearchService.findRelatedArticles(relatedSites, articleKey, ws.getLanguage());
			LOG.debug(RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE, keys.size(), articleKey);
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<ArticleKey> findArticles(List<EntityKey> siteKeys, EntityKey workingSetKey, String searchQuery) {
	    if (siteKeys == null) {
	        throw new IllegalArgumentException(PARAMETER_SITE_KEYS_MUST_NOT_BE_NULL);
	    }
	    if (searchQuery == null) {
	        throw new IllegalArgumentException(PARAMETER_SEARCH_QUERY_MUST_NOT_BE_NULL);
	    }
	    try {
	    	WorkingSet ws = workingSetStore.getWorkingSet(workingSetKey);
	    	return articleSearchService.findArticles(siteKeys, searchQuery, ws.getLanguage());
        } catch (Exception exception) {
            String message = String.format(COULD_NOT_FIND_ARTICLES_FOR_SEARCH, searchQuery);
            LOG.error(message, exception);
            throw new ServiceException(message);
        }
	}
	
	protected EntityContent<ArticleKey> render(ArticleKey key, String template) throws StoreException {
	    return renderer.render(
                key, 
                articleStore.getArticle(key, true), 
                template);
	}
	
	protected List<EntityContent<ArticleKey>> render(List<ArticleKey> keys, String template) throws StoreException {
        return renderer.render(
                keys, 
                articleStore.getArticles(keys, true), 
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