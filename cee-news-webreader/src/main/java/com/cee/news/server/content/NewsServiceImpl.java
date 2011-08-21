package com.cee.news.server.content;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.cee.news.client.content.NewsService;
import com.cee.news.client.list.EntityKey;
import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.server.EntityKeyConversions;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.cee.news.store.WorkingSetStore;

public class NewsServiceImpl implements NewsService {

	private static final long serialVersionUID = -1910608040966050631L;

	private ArticleStore articleStore;

	private SiteStore siteStore;

	private WorkingSetStore workingSetStore;
	
	public void setArticleStore(ArticleStore articleStore) {
		this.articleStore = articleStore;
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
	
	public List<EntityKey> getArticlesOfSite(String siteName) {
		try {
			Site site = siteStore.getSite(siteName);
			return EntityKeyConversions.createEntityKeys(articleStore.getArticlesOrderedByDate(site));
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@Override
	public List<EntityKey> getArticlesOfWorkingSet(String workingSetName) {
		try {
			WorkingSet workingSet = workingSetStore.getWorkingSet(workingSetName);
			return EntityKeyConversions.createEntityKeys(articleStore.getArticlesOrderedByDate(workingSet));
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}

	//TODO: Use Velocity to render simple HTML content
	@Override
	public String getHtmlDescription(String articleId) {
		try {
			Article article = articleStore.getArticle(articleId);
			StringBuilder builder = new StringBuilder();
			builder.append("<h3>").append(article.getTitle()).append("</h3>")
					.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>")
					.append("<p>").append(article.getShortText()).append("</p>");
			return builder.toString();
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}

	//TODO: Use Velocity to render simple HTML content
	@Override
	public String getHtmlContent(String articleId) {
		try {
			StringBuilder builder = new StringBuilder();
			Article article = articleStore.getArticle(articleId);
			builder.append("<h1>").append(article.getTitle()).append("</h1>")
					.append("<p>").append(formatDate(article.getPublishedDate())).append("</p>");
			List<TextBlock> content = articleStore.getContent(article.getLocation());
			for (TextBlock textBlock : content) {
				builder.append("<p>").append(textBlock.getContent()).append("</p>");
			}
			return builder.toString();
		} catch (StoreException exception) {
			throw new RuntimeException(exception);
		}
	}
}