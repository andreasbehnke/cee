package com.cee.news.store.lucene;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import com.cee.news.model.EntityKey;
import com.cee.news.model.Feed;
import com.google.gson.reflect.TypeToken;


public interface LuceneConstants {

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
}
