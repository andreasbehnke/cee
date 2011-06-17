package com.cee.news.server.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.content.NewsService;
import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;

public class NewsServiceImpl implements NewsService {

    private static final long serialVersionUID = -1910608040966050631L;

    private ArticleStore articleStore;
    
    private SiteStore siteStore;
    
    public void setArticleStore(ArticleStore articleStore) {
        this.articleStore = articleStore;
    }

    public void setSiteStore(SiteStore siteStore) {
        this.siteStore = siteStore;
    }

    protected List<String> getArticleLocations(Site site) throws StoreException {
        return articleStore.getArticlesOrderedByDate(site);
    }

    //TODO: Articles should be identified by their id, not the index in a changing list!
    protected Article getArticle(String siteName, int index) throws StoreException {
        Site site = siteStore.getSite(siteName);
        return articleStore.getArticle(getArticleLocations(site).get(index));
    }

    public List<String> getHeadlines(String siteName) {
        try {
            Site site = siteStore.getSite(siteName);
            List<String> articles = getArticleLocations(site);
            List<String> headlines = new ArrayList<String>();
            for (String articleLocation : articles) {
                headlines.add(articleStore.getArticle(articleLocation).getTitle());
            }
            return headlines;
        } catch (StoreException exception) {
            throw new RuntimeException(exception);
        }
    }

    public SafeHtml getHtmlDescription(String siteName, int index) {
        try {
            Article article = getArticle(siteName, index);
            SafeHtmlBuilder builder = new SafeHtmlBuilder();
            builder.appendHtmlConstant("<h3>");
            builder.appendEscaped(article.getTitle());
            builder.appendHtmlConstant("</h3>");
            builder.appendHtmlConstant("<div class=\"date\">");
            //TODO localized date format
            builder.appendEscaped(SimpleDateFormat.getDateInstance().format(article.getPublishedDate().getTime()));
            builder.appendHtmlConstant("</div>");
            builder.appendHtmlConstant("<div class=\"description\">");
            builder.append(SimpleHtmlSanitizer.sanitizeHtml(article.getShortText()));
            builder.appendHtmlConstant("</div>");
            return builder.toSafeHtml();
        } catch (StoreException exception) {
            throw new RuntimeException(exception);
        }
    }

    public SafeHtml getHtmlContent(String siteName, int index) {
        try {
            SafeHtmlBuilder builder = new SafeHtmlBuilder();
            Article article = getArticle(siteName, index);
            builder.appendHtmlConstant("<h1>");
            builder.appendEscaped(article.getTitle());
            builder.appendHtmlConstant("</h1>");
            builder.appendHtmlConstant("<div class=\"date\">");
            //TODO localized date format
            builder.appendEscaped(SimpleDateFormat.getDateInstance().format(article.getPublishedDate().getTime()));
            builder.appendHtmlConstant("</div>");
            List<TextBlock> content = articleStore.getContent(article.getLocation());
            for (TextBlock textBlock : content) {
                builder.appendHtmlConstant("<div>");
                builder.appendEscaped(textBlock.getContent());
                builder.appendHtmlConstant("</div>");
            }
            return builder.toSafeHtml();
        } catch (StoreException exception) {
            throw new RuntimeException(exception);
        }
    }
}