package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.content.EntityContent;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContentModel<K> {

    /**
     * @param key of the entity
     * @param target The HTML container for the title.
     */
    void getContentTitle(HasSafeHtml target, K key);
    
    /**
     * @param target The HTML container for the description text
     * @param key Content key
     */
    void getContentDescription(HasSafeHtml target, K key);
    
    /**
     * @param key of the main content to be displayed
     * @param target The HTML container for the main content
     */
    void getContent(HasSafeHtml target, K key);
}