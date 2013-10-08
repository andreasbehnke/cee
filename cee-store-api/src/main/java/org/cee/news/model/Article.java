package org.cee.news.model;

/*
 * #%L
 * Content Extraction Engine - News Store API
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


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Article {

    private String externalId;
    
    private String language;

    private String location;

    private String title;

    private String shortText;

    private List<TextBlock> content = new ArrayList<TextBlock>();

    private Calendar publishedDate;
    
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String id) {
        this.externalId = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public List<TextBlock> getContent() {
        return content;
    }

    public void setContent(List<TextBlock> content) {
        this.content = content;
    }
    
    public String getContentText() {
    	if (content == null) {
    		return "";
    	}
    	StringBuilder buffer = new StringBuilder();
    	for (TextBlock block : content) {
			buffer.append(block.getContent()).append('\n');
		}
    	return buffer.toString();
    }

    public Calendar getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Calendar pubDate) {
        this.publishedDate = pubDate;
    }
    
    /**
     * @return the ISO 639-1 Language Code of this working set
     */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public String toString() {
	    return "Article [externalId=" + externalId + "; title=" + title + "; language=" + language + "]";
	}
}