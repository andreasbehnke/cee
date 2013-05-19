package com.cee.news.store.lucene;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.google.gson.reflect.TypeToken;


public abstract class LuceneConstants {
	
	public final static Version VERSION = Version.LUCENE_43;

	public final static int MAX_RESULT_SIZE = 500;
	
	public final static Type ENTITY_KEY_LIST_TYPE = new TypeToken<List<EntityKey>>(){}.getType();
	
	
	public static final String FIELD_SITE_NAME = "name";
	
	public static final String FIELD_SITE_LANGUAGE = "language";
	
	public static final String FIELD_SITE_LOCATION = "location";

	public static final String FIELD_SITE_TITLE = "title";

	public static final String FIELD_SITE_DESCRIPTION = "description";

	public static final String FIELD_SITE_FEEDS = "feeds";
	
	public final static Type FEED_LIST_TYPE = new TypeToken<List<Feed>>(){}.getType();

	public final static Sort SITE_NAME_SORT = new Sort(new SortField(FIELD_SITE_NAME, SortField.Type.STRING, false));


	public final static String FIELD_WORKING_SET_NAME = "name";
	
	public final static String FIELD_WORKING_SET_LANGUAGE = "language";
	
	public final static String FIELD_WORKING_SET_SITES = "sites";
	
	public final static Sort WORKING_SET_NAME_SORT = new Sort(new SortField(FIELD_WORKING_SET_NAME, SortField.Type.STRING, false));

	
	public final static String FIELD_ARTICLE_SITE = "site";

	public final static String FIELD_ARTICLE_EXTERNAL_ID = "externalId";
    
	public final static String FIELD_ARTICLE_LANGUAGE = "language";
    
	public final static String FIELD_ARTICLE_LOCATION = "location";
    
	public final static String FIELD_ARTICLE_TITLE = "title";
    
	public final static String FIELD_ARTICLE_SHORT_TEXT = "shortText";
    
	public final static String FIELD_ARTICLE_CONTENT = "content";
    
	public final static String FIELD_ARTICLE_PUBLISHED_DATE = "publishedDate";
	
	public final static Sort ARTICLE_PUBLISHED_SORT = new Sort(new SortField(FIELD_ARTICLE_PUBLISHED_DATE, SortField.Type.STRING, true));
	
	public final static String[] ARTICLE_FULLTEXT_SEARCH_FIELDS = {FIELD_ARTICLE_CONTENT, FIELD_ARTICLE_SHORT_TEXT, FIELD_ARTICLE_TITLE};

	public final static Map<String, Float> ARTICLE_FULLTEXT_SEARCH_BOOSTS = new HashMap<String, Float>();
	
	static {
		ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_CONTENT,    Float.valueOf(1f));
		ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_SHORT_TEXT, Float.valueOf(1.5f));
		ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_TITLE,      Float.valueOf(2f));
	}
	
	public final static String[] ARTICLE_RELATED_SEARCH_FIELDS = {FIELD_ARTICLE_TITLE, FIELD_ARTICLE_SHORT_TEXT, FIELD_ARTICLE_CONTENT}; 
}
