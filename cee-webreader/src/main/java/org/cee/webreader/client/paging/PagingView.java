package org.cee.webreader.client.paging;

/*
 * #%L
 * News Reader
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


import java.util.List;

import org.cee.webreader.client.list.KeyLink;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.HasSafeHtml;

/**
 * A pageable view displays content and links for navigating to the previous or next content.
 * An additional content selection for direct jumps is also available.
 */
public interface PagingView {

    /**
     * @param enabled If true, the previous link is enabled
     */
    void setPreviousEnabled(boolean enabled);
    
    /**
     * @return The html content of the previous link
     */
    HasSafeHtml getPreviousContent();
    
    /**
     * @param handler Handler will be notified if the previous link is clicked
     */
    void addPreviousClickHandler(ClickHandler handler);
    
    /**
     * @param enabled If true, the next link is enabled
     */
    void setNextEnabled(boolean enabled);
    
    /**
     * @return The html content of the next link
     */
    HasSafeHtml getNextContent();
    
    /**
     * @param handler Handler will be notified if the next link is clicked
     */
    void addNextClickHandler(ClickHandler handler);
    
    /**
     * @return The html content of the main content area
     */
    HasSafeHtml getMainContent();
    
    /**
     * Resets the scroll position to 0
     */
    void resetMainContentScrollPosition();
    
    /**
     * @param links The list of links which should be displayed in e.g. a selection box
     */
    void setJumpToLinks(List<KeyLink> links);
    
    /**
     * @param index The currently selected index of the selection box
     */
    void setJumpToSelectedIndex(int index);
    
    int getJumpToSelectedIndex();
    
    /**
     * @param handler Handler will be notified if the user selected another content from the selection box
     */
    void addJumpToChangedHandler(ChangeHandler handler);
}
