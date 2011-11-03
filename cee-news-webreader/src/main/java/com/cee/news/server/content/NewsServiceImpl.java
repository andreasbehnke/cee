package com.cee.news.server.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.client.content.NewsService;
import com.cee.news.client.list.EntityKeyUtil;
import com.cee.news.model.Article;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.search.ArticleSearchService;
import com.cee.news.search.SearchException;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class NewsServiceImpl implements NewsService {

	private static final Logger LOG = LoggerFactory.getLogger(NewsServiceImpl.class);
	
	private static final long serialVersionUID = -1910608040966050631L;

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
	
	@Override
	public List<EntityKey> getArticlesOfSite(String siteKey) {
		try {
			Site site = siteStore.getSite(siteKey);
			List<EntityKey> keys = articleStore.getArticlesOrderedByDate(site);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Retrieved {} articles for site {}", keys.size(), siteKey);
			}
			return keys;
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@Override
	public List<EntityKey> getArticlesOfWorkingSet(String workingSetName) {
		try {
			WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetName);
			List<EntityKey> keys = articleStore.getArticlesOrderedByDate(workingSet);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Retrieved {} articles for working set {}", keys.size(), workingSetName);
			}
			return keys;
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@Override
	public List<EntityKey> getRelatedArticles(String articleId, String workingSet) {
		try {
			WorkingSet ws = workingSetStore.getWorkingSet(workingSet);
			if (ws == null) {
				throw new IllegalArgumentException("Working set " + workingSet + " not found");
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
				LOG.debug("Retrieved {} related articles for article {}", keys.size(), articleId);
			}
			return keys;
		} catch (SearchException exception) {
			throw new RuntimeException(exception);
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}

	//TODO: Use Velocity to render simple HTML content
	@Override
	public String getHtmlDescription(EntityKey articleKey) {
		try {
			Article article = articleStore.getArticle(articleKey.getKey());
			StringBuilder builder = new StringBuilder();
			builder.append("<h3>").append(article.getTitle()).append("</h3>")
					.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>");
			if (articleKey.getScore() != -1) {
				builder.append("<p>").append(articleKey.getScore()).append("</p>");
			}
			builder.append("<p>").append(article.getShortText()).append("</p>");
			return builder.toString();
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}

	//TODO: Use Velocity to render simple HTML content
	@Override
	public String getHtmlContent(EntityKey articleKey) {
		try {
			StringBuilder builder = new StringBuilder();
			Article article = articleStore.getArticle(articleKey.getKey());
			builder.append("<h1>").append(article.getTitle()).append("</h1>")
					.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>")
					.append("<p><a href=\"").append(article.getLocation()).append("\" target=\"article\">open article</a></p>");
			List<TextBlock> content = articleStore.getContent(articleKey.getKey());
			for (TextBlock textBlock : content) {
				builder.append("<p>").append(textBlock.getContent()).append("</p>");
			}
			return builder.toString();
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}
}