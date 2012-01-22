package com.cee.news.server.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.EntityKeyUtil;
import com.cee.news.client.content.NewsService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.WorkingSetStore;

public class NewsServiceImpl implements NewsService {

	private static final Logger LOG = LoggerFactory.getLogger(NewsServiceImpl.class);

	private static final String COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST = "Could not retrieve contents for key list";

	private static final String COULD_NOT_RETRIEVE_CONTENT_FOR = "Could not retrieve content for %s";

	private static final String COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR = "Could not retrieve related articles for %s";

	private static final String RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE = "Retrieved %s related articles for article %s";

	private static final String WORKING_SET_NOT_FOUND = "Working set %s not found";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET = "Could not retrieve articles of working set %s";

	private static final String RETRIEVED_ARTICLES_FOR_WORKING_SET = "Retrieved %s articles for working set %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITE = "Retrieved %s articles for site %s";

	private static final String RETRIEVED_ARTICLES_FOR_SITES = "Retrieved %s articles for %s sites";
	
	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITE = "Could not retrieve articles of site %s";
	
	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITES = "Could not retrieve articles of sites";
    
    private ArticleStore articleStore;

	private ArticleSearchService articleSearchService;
	
	private SiteStore siteStore;

	private WorkingSetStore workingSetStore;
	
	public void setArticleStore(ArticleStore articleStore) {
		this.articleStore = articleStore;
	}
	
	public void setArticleSearchService(ArticleSearchService articleSearchService) {
		this.articleSearchService = articleSearchService;
	}

	public void setSiteStore(SiteStore siteStore) {
		this.siteStore = siteStore;
	}
	
	public void setWorkingSetStore(WorkingSetStore workingSetStore) {
		this.workingSetStore = workingSetStore;
	}

	// TODO localized date format
	protected String formatDate(Calendar calendar) {
		return SimpleDateFormat.getDateInstance().format(calendar.getTime());
	}
	
	protected EntityKey renderDescription(Article article, EntityKey articleKey) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h3>").append(article.getTitle()).append("</h3>")
				.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>");
		if (articleKey.getScore() != -1) {
			builder.append("<p>").append(articleKey.getScore()).append("</p>");
		}
		builder.append("<p>").append(article.getShortText()).append("</p>");
		articleKey.setHtmlContent(builder.toString());
		return articleKey;
	}

	protected EntityKey renderContent(Article article, EntityKey articleKey) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h1>").append(article.getTitle()).append("</h1>")
			.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>")
			.append("<p><a href=\"").append(article.getLocation()).append("\" target=\"article\">open article</a></p>");
		for (TextBlock textBlock : article.getContent()) {
			builder.append("<p>").append(textBlock.getContent()).append("</p>");
		}
		articleKey.setHtmlContent(builder.toString());
        return articleKey;
	}
	
	@Override
	public List<EntityKey> getArticlesOfSite(String siteKey) {
	    if (siteKey == null) {
	        throw new IllegalArgumentException("Parameter siteKey must not be null");
	    }
		try {
			Site site = siteStore.getSite(siteKey);
			List<EntityKey> keys = articleStore.getArticlesOrderedByDate(site);
			if (LOG.isDebugEnabled()) {
				LOG.debug(RETRIEVED_ARTICLES_FOR_SITE, keys.size(), siteKey);
			}
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_SITE, siteKey);
			LOG.error(message , exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityKey> getArticlesOfSites(List<String> siteKeys) {
	    if (siteKeys == null) {
            throw new IllegalArgumentException("Parameter siteKeys must not be null");
        }
	    try {
            List<Site> sites = siteStore.getSites(siteKeys);
            List<EntityKey> keys = articleStore.getArticlesOrderedByDate(sites);
            if (LOG.isDebugEnabled()) {
                LOG.debug(RETRIEVED_ARTICLES_FOR_SITES, keys.size(), sites.size());
            }
            return keys;
        } catch (Exception exception) {
            LOG.error(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES , exception);
            throw new ServiceException(COULD_NOT_RETRIEVE_ARTICLES_OF_SITES);
        }
	}
	
	@Override
	public List<EntityKey> getArticlesOfWorkingSet(String workingSetName) {
	    if (workingSetName == null) {
            throw new IllegalArgumentException("Parameter workingSetName must not be null");
        }
		try {
			WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetName);
			List<EntityKey> keys = articleStore.getArticlesOrderedByDate(workingSet);
			if (LOG.isDebugEnabled()) {
				LOG.debug(RETRIEVED_ARTICLES_FOR_WORKING_SET, keys.size(), workingSetName);
			}
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET, workingSetName);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityKey> getRelatedArticles(String articleId, String workingSet) {
	    if (articleId == null) {
            throw new IllegalArgumentException("Parameter articleId must not be null");
        }
	    if (workingSet == null) {
            throw new IllegalArgumentException("Parameter workingSet must not be null");
        }
        try {
			WorkingSet ws = workingSetStore.getWorkingSet(workingSet);
			if (ws == null) {
				throw new IllegalArgumentException(String.format(WORKING_SET_NOT_FOUND, workingSet));
			}
			List<EntityKey> sites = ws.getSites();
			List<EntityKey> relatedSites = null;
			if (sites.size() == 1) {
				relatedSites = sites;
			} else {
				//remove site of current article
				relatedSites = new ArrayList<EntityKey>(sites);
				relatedSites.remove(new EntityKey(null, articleStore.getSiteKey(articleId)));
			}
			List<EntityKey> keys = articleSearchService.findRelatedArticles(EntityKeyUtil.extractKeys(relatedSites), articleId);
			if (LOG.isDebugEnabled()) {
				LOG.debug(RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE, keys.size(), articleId);
			}
			return keys;
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR, articleId);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public EntityKey getHtmlDescription(EntityKey articleKey) {
	    if (articleKey == null) {
            throw new IllegalArgumentException("Parameter articleKey must not be null");
        }
	    try {
			Article article = articleStore.getArticle(articleKey.getKey(), false);
			return renderDescription(article, articleKey);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_CONTENT_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityKey> getHtmlDescriptions(List<EntityKey> keys) {
	    if (keys == null) {
            throw new IllegalArgumentException("Parameter keys must not be null");
        }
	    try {
			for (EntityKey key : keys) {
				Article article = articleStore.getArticle(key.getKey(), false);
				renderDescription(article, key);
			}
			return keys;
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
	
	@Override
	public EntityKey getHtmlContent(EntityKey articleKey) {
	    if (articleKey == null) {
            throw new IllegalArgumentException("Parameter articleKey must not be null");
        }
	    try {
			Article article = articleStore.getArticle(articleKey.getKey(), true);
			return renderContent(article, articleKey);
		} catch (Exception exception) {
			String message = String.format(COULD_NOT_RETRIEVE_CONTENT_FOR, articleKey);
			LOG.error(message, exception);
			throw new ServiceException(message);
		}
	}
	
	@Override
	public List<EntityKey> getHtmlContents(ArrayList<EntityKey> keys) {
	    if (keys == null) {
            throw new IllegalArgumentException("Parameter keys must not be null");
        }
	    try {
			for (EntityKey key : keys) {
				Article article = articleStore.getArticle(key.getKey(), false);
				renderContent(article, key);
			}
			return keys;
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
}