package com.cee.news.client.list;

import com.google.gwt.safehtml.client.HasSafeHtml;

public interface ContentListModel extends ListModel {

    /**
     * @param index of the title
     * @param target The HTML container for the title.
     */
    void getContentTitle(HasSafeHtml target, int index);
    
    /**
     * @param target The HTML container for the description text
     * @param index Content index
     */
    void getContentDescription(HasSafeHtml target, int index);
}