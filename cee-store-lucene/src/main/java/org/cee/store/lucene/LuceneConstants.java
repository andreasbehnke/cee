package org.cee.store.lucene;

/*
 * #%L
 * Content Extraction Engine - News Store Lucene
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.cee.news.model.EntityKey;
import org.cee.news.model.Feed;

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
		//ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_CONTENT,    Float.valueOf(1f));
		ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_SHORT_TEXT, Float.valueOf(1.5f));
		ARTICLE_FULLTEXT_SEARCH_BOOSTS.put(FIELD_ARTICLE_TITLE,      Float.valueOf(2.5f));
	}
	
	public final static String[] ARTICLE_RELATED_SEARCH_FIELDS = {FIELD_ARTICLE_TITLE, FIELD_ARTICLE_SHORT_TEXT};//, FIELD_ARTICLE_CONTENT}; 
}
