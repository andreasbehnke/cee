/**
 * 
 */
package org.cee.store.jcr;

import static org.cee.store.jcr.JcrStoreConstants.NODE_ARTICLE;
import static org.cee.store.jcr.JcrStoreConstants.NODE_CONTENT;
import static org.cee.store.jcr.JcrStoreConstants.NODE_TEXTBLOCK;
import static org.cee.store.jcr.JcrStoreConstants.PROP_CONTENT;
import static org.cee.store.jcr.JcrStoreConstants.PROP_ID;
import static org.cee.store.jcr.JcrStoreConstants.PROP_LANGUAGE;
import static org.cee.store.jcr.JcrStoreConstants.PROP_LOCATION;
import static org.cee.store.jcr.JcrStoreConstants.PROP_PUBLISHED;
import static org.cee.store.jcr.JcrStoreConstants.PROP_SHORT_TEXT;
import static org.cee.store.jcr.JcrStoreConstants.PROP_TITLE;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.RowIterator;

import org.apache.jackrabbit.util.Text;
import org.cee.news.model.Article;
import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.news.model.TextBlock;
import org.cee.news.model.WorkingSet;
import org.cee.news.store.ArticleChangeListener;
import org.cee.news.store.ArticleChangeListenerSupport;
import org.cee.news.store.ArticleStore;
import org.cee.news.store.StoreException;

/**
 * JCR implementation of the store {@link NewsStore}
 */
public class JcrArticleStore extends JcrStoreBase implements ArticleStore {

	private final static String SELECT_ARTICLES_OF_SITE_ORDERED_BY_DATE = "SELECT s.[news:name] AS " + SITE_NAME_SELECTOR + ", a.[news:id] AS " + ARTICLE_ID_SELECTOR + ", a.[news:title] AS " + ARTICLE_TITLE_SELECTOR + " "
                                                                        + "FROM [news:article] AS a INNER JOIN [news:site] AS s ON ISCHILDNODE(a, s) "
                                                                        + "WHERE %s " 
                                                                        + "ORDER BY a.[news:published] DESC";
    
	private ArticleChangeListenerSupport listenerSupport = new ArticleChangeListenerSupport();
	
    public JcrArticleStore() {
    }

    public JcrArticleStore(SessionManager sessionManager) {
		setSessionManager(sessionManager);
	}
    
    protected Node getArticleNode(ArticleKey articleKey) throws RepositoryException {
        if (articleKey == null) {
            throw new IllegalArgumentException("Parameter articleKey must not be null");
        }
        return getContentNodeOrNull(buildArticlePath(articleKey));
    }

    @Override
    public ArticleKey update(EntityKey site, Article article) throws StoreException {
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        if (article == null) {
            throw new IllegalArgumentException("Parameter article must not be null");
        }
        Node articleNode = null;
        String siteKey = site.getKey();
        String articleId = article.getExternalId();
        ArticleKey articleKey = ArticleKey.get(article.getTitle(), articleId, siteKey);
        
        try {
            articleNode = getArticleNode(articleKey);
        } catch (RepositoryException e) {
            throw new StoreException(article, "Could not retrive article node", e);
        }
        
        if (articleNode == null) {
        	Node siteNode = null;
            try {
                siteNode = getContentNodeOrNull(Text.escapeIllegalJcrChars(siteKey));
            } catch (RepositoryException e) {
                throw new StoreException(site, "Could not find site node", e);
            }
            if (siteNode == null) {
                throw new StoreException(site, "Site does not exists");
            }
            try {
                articleNode = siteNode.addNode(Text.escapeIllegalJcrChars(articleId), NODE_ARTICLE);
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
            articleNode.setProperty(PROP_ID, articleId);
            String title = article.getTitle();
            String shortText = article.getShortText();
            if (shortText == null) {
                shortText = title;
            }
            articleNode.setProperty(PROP_SHORT_TEXT, shortText);
            articleNode.setProperty(PROP_TITLE, title);
            articleNode.setProperty(PROP_LANGUAGE, article.getLanguage());
            articleNode.setProperty(PROP_LOCATION, article.getLocation());
            articleNode.setProperty(PROP_PUBLISHED, article.getPublishedDate());
            for (TextBlock text : article.getContent()) {
                Node textNode = articleNode.addNode(NODE_CONTENT, NODE_TEXTBLOCK);
                textNode.setProperty(PROP_CONTENT, text.getContent());
            }
        } catch (RepositoryException e) {
            throw new StoreException(article, "Could not persist article", e);
        }
        try {
            getSession().save();
            listenerSupport.fireArticleChanged(site, article);
        } catch (RepositoryException e) {
            throw new StoreException(site, "Could not save session", e);
        }
        return articleKey;
    }
    
    @Override
    public boolean contains(EntityKey site, String externalId) throws StoreException {
    	ArticleKey articleKey = ArticleKey.get(null, externalId, site.getKey());
        try {
	        return containsContentNode(buildArticlePath(articleKey));
        } catch (RepositoryException e) {
	        throw new StoreException(null, e);
        }
    }
    
    @Override
    public List<ArticleKey> addNewArticles(EntityKey site, List<Article> articles) throws StoreException {
    	if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        if (articles == null) {
            throw new IllegalArgumentException("Parameter articles must not be null");
        }
        List<ArticleKey> keys = new ArrayList<ArticleKey>();
        for (Article article : articles) {
        	try {
    	    	ArticleKey key = ArticleKey.get(article.getTitle(), article.getExternalId(), site.getKey());
		        if (!containsContentNode(buildArticlePath(key))) {
		        	keys.add(update(site, article));
		        }
        	} catch (RepositoryException re) {
            	throw new StoreException(article, "Could not check article exists");
            }
        }
        return keys;
    }
    
    protected List<TextBlock> createContentFromNode(Node articleNode) throws StoreException {
    	List<TextBlock> content = new ArrayList<TextBlock>();
        try {
            NodeIterator iter = articleNode.getNodes(NODE_CONTENT);
            while (iter.hasNext()) {
                Node textNode = iter.nextNode();
                content.add(new TextBlock(textNode.getProperty(PROP_CONTENT).getString()));
            }
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve text blocks of article", e);
        }
        return content;
    }
    
    protected Article createArticleFromNode(Node articleNode, boolean deep) throws StoreException {
    	if (articleNode == null) {
            return null;
        } else {
            Article article = new Article();
            try {
                article.setExternalId(articleNode.getProperty(PROP_ID).getString());
                article.setLanguage(getStringPropertyOrNull(articleNode, PROP_LANGUAGE));
                article.setLocation(articleNode.getProperty(PROP_LOCATION).getString());
                article.setPublishedDate(articleNode.getProperty(PROP_PUBLISHED).getDate());
                if (articleNode.hasProperty(PROP_TITLE)) {
                	article.setTitle(articleNode.getProperty(PROP_TITLE).getString());
                }
                if (articleNode.hasProperty(PROP_SHORT_TEXT)) {
                	article.setShortText(articleNode.getProperty(PROP_SHORT_TEXT).getString());
                }
                if (deep) {
                	article.setContent(createContentFromNode(articleNode));
                }
                
            } catch (RepositoryException e) {
                throw new StoreException("Could not set article properties", e);
            }
            return article;
        }
    }

    @Override
    public Article getArticle(ArticleKey articleKey, boolean withContent) throws StoreException {
        if (articleKey == null) {
            throw new IllegalArgumentException("Parameter articleKey must not be null");
        }
        Node articleNode = null;
        try {
            articleNode = getArticleNode(articleKey);
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrive article node", e);
        }

        return createArticleFromNode(articleNode, withContent);
    }
    
    @Override
    public List<Article> getArticles(List<ArticleKey> keys, boolean withContent) throws StoreException {
    	List<Article> articles = new ArrayList<Article>();
    	for (ArticleKey key : keys) {
    		articles.add(getArticle(key, true));
    	}
    	return articles;
    }
    
    protected RowIterator getArticlesOfSitesOrderedByPublication(List<EntityKey> sites) throws RepositoryException {
        QueryManager queryManager = getSession().getWorkspace().getQueryManager();
        String whereExpression = buildExpression(WHERE_SITE_NAME_TERM, OR, sites);
        Query q = queryManager.createQuery(String.format(SELECT_ARTICLES_OF_SITE_ORDERED_BY_DATE, whereExpression), Query.JCR_SQL2);
        q.setOffset(0);
        q.setLimit(DEFAULT_QUERY_LIMIT);
        return q.execute().getRows();
    }
    
    @Override
    public List<ArticleKey> getArticlesOrderedByDate(EntityKey siteKey) throws StoreException {
        if (siteKey == null) {
            throw new IllegalArgumentException("Parameter siteKey must not be null");
        }
        try {
            List<EntityKey> keys = new ArrayList<EntityKey>();
            keys.add(siteKey);
            return buildArticleList(getArticlesOfSitesOrderedByPublication(keys));
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve articles", e);
        }
    }
    
    @Override
    public List<ArticleKey> getArticlesOrderedByDate(List<EntityKey> keys) throws StoreException {
        if (keys == null) {
            throw new IllegalArgumentException("Parameter sites must not be null");
        }
        if (keys.isEmpty()) {
            return new ArrayList<ArticleKey>();
        }
        try {
            return buildArticleList(getArticlesOfSitesOrderedByPublication(keys));
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve articles", e);
        }
    }
    
    @Override
    public List<ArticleKey> getArticlesOrderedByDate(WorkingSet workingSet) throws StoreException {
    	if (workingSet == null) {
            throw new IllegalArgumentException("Parameter workingSet must not be null");
        }
    	if (workingSet.getSites() == null || workingSet.getSites().isEmpty()) {
    		return new ArrayList<ArticleKey>();
    	}
        try {
        	return buildArticleList(getArticlesOfSitesOrderedByPublication(workingSet.getSites()));
        } catch (RepositoryException e) {
            throw new StoreException("Could not retrieve articles", e);
        }
    }
    
    @Override
    public void addArticleChangeListener(ArticleChangeListener listener) {
    	listenerSupport.addArticleChangeListener(listener);
    }
}