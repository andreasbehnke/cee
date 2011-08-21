package com.cee.news.store.jcr;

/**
 * Constants define the names of the used nodes and node properties
 */
public final class JcrStoreConstants {
    
    public static final String PROP_WORDS = "news:words";

    public static final String PROP_CONTENT = "news:content";

    public static final String PROP_PUBLISHED = "news:published";

    public static final String PROP_SHORT_TEXT = "news:shortText";

    public static final String PROP_ID = "news:id";

    public static final String PROP_ACTIVE = "news:active";

    public static final String PROP_CONTENT_TYPE = "news:contentType";

    public static final String PROP_TITLE = "news:title";

    public static final String PROP_LOCATION = "news:location";

    public static final String PROP_DESCRIPTION = "news:description";
    
    public static final String PROP_NAME = "news:name";
    
    public static final String PROP_SITES = "news:sites";

    public static final String NODE_CONTENT = "news:content";

    public static final String NODE_FEED = "news:feed";

    public static final String NODE_SITE = "news:site";

    public static final String NODE_ARTICLE = "news:article";

    public static final String NODE_TEXTBLOCK = "news:textblock";
    
    public static final String NODE_WORKINGSET = "news:workingSet";
    
    public static final String SLASH = "/";

    private JcrStoreConstants() {}
}
