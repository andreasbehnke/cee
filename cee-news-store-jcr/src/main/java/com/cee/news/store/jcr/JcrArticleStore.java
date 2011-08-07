/**
 * 
 */
package com.cee.news.store.jcr;

import static com.cee.news.store.jcr.JcrStoreConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.RowIterator;

import com.cee.news.model.Article;
import com.cee.news.model.Site;
import com.cee.news.model.TextBlock;
import com.cee.news.model.WorkingSet;
import com.cee.news.store.ArticleStore;
import com.cee.news.store.StoreException;

/**
 * JCR implementation of the store {@link NewsStore}
 */
public class JcrArticleStore extends JcrStoreBase implements ArticleStore {

    private final static String SELECT_ARTICLE_BY_LOCATION = "SELECT * FROM [news:article] WHERE [news:id]='%s'";

    private final static String SELECT_ARTICLES_OF_SITE_ORDERED_BY_DATE = "SELECT a.[news:id] AS id " 
                                                                        + "FROM [news:article] AS a INNER JOIN [news:site] AS s ON ISCHILDNODE(a,s) "
                                                                        + "WHERE %s " 
                                                                        + "ORDER BY a.[news:published] DESC";
    
    private final static String OR = "OR ";
    
    private final static String WHERE_SITE_NAME_TERM = "s.[news:name] = '%s' ";

    public JcrArticleStore() {
    }

    public JcrArticleStore(Session session) throws StoreException {
        setSession(session);
    }
    
    protected Node getArticleNode(String id) throws RepositoryException {
        if (id == null) {
            throw new IllegalArgumentException("Parameter id must not be null");
        }
        testSession();
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        Query q = queryManager.createQuery(String.format(SELECT_ARTICLE_BY_LOCATION, id), Query.JCR_SQL2);
        NodeIterator iter = q.execute().getNodes();
        if (iter.hasNext()) {
            Node articleNode = iter.nextNode();
            if (iter.hasNext()) {
                throw new IllegalStateException("There must only one article with same ID");
            }
            return articleNode;
        } else {
            return null;
        }
    }

    public void update(Site site, Article article) throws StoreException {
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        if (article == null) {
            throw new IllegalArgumentException("Parameter article must not be null");
        }
        Node articleNode = null;
        try {
            articleNode = getArticleNode(article.getId());
        } catch (RepositoryException e) {
            throw new StoreException(article, "Could not retrive article node", e);
        }

        if (articleNode == null) {
            Node siteNode = null;
            try {
                siteNode = getSiteNode(site.getName());
            } catch (RepositoryException e) {
                throw new StoreException(site, "Could not find site node", e);
            }
            if (siteNode == null) {
                throw new StoreException(site, "Site does not exists");
            }
            try {
                articleNode = siteNode.addNode(NODE_ARTICLE, NODE_ARTICLE);
            } catch (RepositoryException e) {
                throw new StoreException(article, "Could not add article to site", e);
            }
        } else {
            //remove existing content for re-adding
            try {
                NodeIterator iter = articleNode.getNodes(NODE_CONTENT);
                while(iter.hasNext()) {
                    iter.nextNode().remove();
                }
                getSession().save();
            } catch (RepositoryException e) {
                throw new StoreException(article, "Could not remove existing content from node", e);
            }
        }

        try {
            articleNode.setProperty(PROP_ID, article.getId());
            String title = article.getTitle();
            String shortText = article.getShortText();
            if (shortText == null) {
                shortText = title;
            }
            articleNode.setProperty(PROP_SHORT_TEXT, shortText);
            articleNode.setProperty(PROP_TITLE, title);
            articleNode.setProperty(PROP_LOCATION, article.getLocation());
            articleNode.setProperty(PROP_PUBLISHED, article.getPublishedDate());
            for (TextBlock text : article.getContent()) {
                Node textNode = articleNode.addNode(NODE_CONTENT, NODE_TEXTBLOCK);
                textNode.setProperty(PROP_CONTENT, text.getContent());
                textNode.setProperty(PROP_WORDS, text.getNumWords());
            }
        } catch (RepositoryException e) {
            throw new StoreException(article, "Could not persist article", e);
        }
        try {
            getSession().save();
        } catch (RepositoryException e) {
            throw new StoreException(site, "Could not save session", e);
        }
    }

    public Article getArticle(String id) throws StoreException {
        if (id == null) {
            throw new IllegalArgumentException("Parameter location must not be null");
        }
        Node articleNode = null;
        try {
            articleNode = getArticleNode(id);
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrive article node", e);
        }

        if (articleNode == null) {
            return null;
        } else {
            Article article = new Article();
            try {
                article.setId(articleNode.getProperty(PROP_ID).getString());
                article.setLocation(articleNode.getProperty(PROP_LOCATION).getString());
                article.setPublishedDate(articleNode.getProperty(PROP_PUBLISHED).getDate());
                article.setShortText(articleNode.getProperty(PROP_SHORT_TEXT).getString());
                article.setTitle(articleNode.getProperty(PROP_TITLE).getString());
            } catch (RepositoryException e) {
                throw new StoreException("Could not set article properties", e);
            }
            return article;
        }
    }
    
    protected String buildExpression(String formatStr, String operator, List<String> keys) {
    	boolean isFirst = true;
    	StringBuilder buffer = new StringBuilder();
    	for (String key : keys) {
			if (isFirst) {
				isFirst = false;
			} else {
				buffer.append(operator);
			}
			buffer.append(String.format(formatStr, key));
		}
    	return buffer.toString();
    }

    protected RowIterator getArticlesOfSitesOrderedByPublication(List<String> siteNames) throws RepositoryException {
        testSession();
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        String whereExpression = buildExpression(WHERE_SITE_NAME_TERM, OR, siteNames);
        Query q = queryManager.createQuery(String.format(SELECT_ARTICLES_OF_SITE_ORDERED_BY_DATE, whereExpression), Query.JCR_SQL2);
        return q.execute().getRows();
    }

    public List<String> getArticlesOrderedByDate(Site site) throws StoreException {
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        try {
            List<String> articles = new ArrayList<String>();
            List<String> sites = new ArrayList<String>();
            sites.add(site.getName());
            RowIterator iter = getArticlesOfSitesOrderedByPublication(sites);
            while (iter.hasNext()) {
                articles.add(iter.nextRow().getValue("id").getString());
            }
            return articles;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve articles", e);
        }
    }
    
    @Override
    public List<String> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException {
    	if (workingSet == null) {
            throw new IllegalArgumentException("Parameter workingSet must not be null");
        }
    	if (workingSet.getSites() == null || workingSet.getSites().isEmpty()) {
    		return new ArrayList<String>();
    	}
        try {
            List<String> articles = new ArrayList<String>();
            RowIterator iter = getArticlesOfSitesOrderedByPublication(workingSet.getSites());
            while (iter.hasNext()) {
                articles.add(iter.nextRow().getValue("id").getString());
            }
            return articles;
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve articles", e);
        }
    }

    public List<TextBlock> getContent(String id) throws StoreException {
        if (id == null) {
            throw new IllegalArgumentException("Parameter id must not be null");
        }
        Node articleNode = null;
        try {
            articleNode = getArticleNode(id);
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve article node", e);
        }
        if (articleNode == null) {
            throw new StoreException("Unknown article");
        }
        List<TextBlock> content = new ArrayList<TextBlock>();
        try {
            NodeIterator iter = articleNode.getNodes(NODE_CONTENT);
            while (iter.hasNext()) {
                Node textNode = iter.nextNode();
                content.add(new TextBlock(textNode.getProperty(PROP_CONTENT).getString(), (int) textNode.getProperty(PROP_WORDS).getLong()));
            }
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve text blocks of article", e);
        }
        return content;
    }
}