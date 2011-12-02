package com.cee.news.server.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.NewsService;
import com.cee.news.client.error.ServiceException;
import com.cee.news.client.list.EntityContent;
import com.cee.news.client.list.EntityKeyUtil;
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

	private static final String COULD_NOT_RETRIEVE_CONTENT_FOR = "Could not retrieve content for {}";

	private static final String COULD_NOT_RETRIEVE_RELATED_ARTICLES_FOR = "Could not retrieve related articles for {}";

	private static final String RETRIEVED_RELATED_ARTICLES_FOR_ARTICLE = "Retrieved {} related articles for article {}";

	private static final String WORKING_SET_NOT_FOUND = "Working set {} not found";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_WORKING_SET = "Could not retrieve articles of working set {}";

	private static final String RETRIEVED_ARTICLES_FOR_WORKING_SET = "Retrieved {} articles for working set {}";

	private static final String RETRIEVED_ARTICLES_FOR_SITE = "Retrieved {} articles for site {}";

	private static final String COULD_NOT_RETRIEVE_ARTICLES_OF_SITE = "Could not retrieve articles of site {}";
	
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
	
	protected EntityContent renderDescription(Article article, EntityKey articleKey) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h3>").append(article.getTitle()).append("</h3>")
				.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>");
		if (articleKey.getScore() != -1) {
			builder.append("<p>").append(articleKey.getScore()).append("</p>");
		}
		builder.append("<p>").append(article.getShortText()).append("</p>");
		return new EntityContent(articleKey, builder.toString());
	}

	protected EntityContent renderContent(Article article, EntityKey articleKey) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h1>").append(article.getTitle()).append("</h1>")
			.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>")
			.append("<p><a href=\"").append(article.getLocation()).append("\" target=\"article\">open article</a></p>");
		for (TextBlock textBlock : article.getContent()) {
			builder.append("<p>").append(textBlock.getContent()).append("</p>");
		}
		return new EntityContent(articleKey, builder.toString());
	}
	
	@Override
	public List<EntityKey> getArticlesOfSite(String siteKey) {
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
	public List<EntityKey> getArticlesOfWorkingSet(String workingSetName) {
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
	public EntityContent getHtmlDescription(EntityKey articleKey) {
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
	public List<EntityContent> getHtmlDescriptions(List<EntityKey> keys) {
		try {
			List<EntityContent> descriptions = new ArrayList<EntityContent>();
			for (EntityKey key : keys) {
				Article article = articleStore.getArticle(key.getKey(), false);
				descriptions.add(renderDescription(article, key));
			}
			return descriptions;
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
	
	@Override
	public EntityContent getHtmlContent(EntityKey articleKey) {
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
	public List<EntityContent> getHtmlContents(ArrayList<EntityKey> keys) {
		try {
			List<EntityContent> contents = new ArrayList<EntityContent>();
			for (EntityKey key : keys) {
				Article article = articleStore.getArticle(key.getKey(), false);
				contents.add(renderContent(article, key));
			}
			return contents;
		} catch (Exception exception) {
			LOG.error(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST, exception);
			throw new ServiceException(COULD_NOT_RETRIEVE_CONTENTS_FOR_KEY_LIST);
		}
	}
}