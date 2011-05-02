package com.cee.news.client.paging;

import com.cee.news.client.list.ContentListModel;
import com.google.gwt.safehtml.client.HasSafeHtml;

/**
 * Manages the content of the pageable panel.
 */
public interface PagingContentModel extends ContentListModel {

    /**
     * @param index of the main content to be displayed
     * @param target The HTML container for the main content
     */
    void getContent(HasSafeHtml target, int index);
}
