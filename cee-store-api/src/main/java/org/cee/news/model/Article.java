package org.cee.news.model;

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