package org.cee.webreader.client.list;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.HasVerticalScrolling;
import com.google.gwt.view.client.HasRows;

/**
 * Increases the visible range of a display when scrolling down
 * @author andreasbehnke
 *
 */
public class IncreaseVisibleRangeScrollHandler implements ScrollHandler, com.google.gwt.user.client.Window.ScrollHandler {
    
    private static final int DEFAULT_INCREMENT = 20;
    
    private static final int DEFAULT_SCROLL_POSITION_OFFSET = 30;
    
    private final int increment;
    
    private final int scrollPositionOffset;
    
    private int lastScrollPos = 0;
    
    private final HasRows display;
    
    private final HasVerticalScrolling viewPort;
    
    public IncreaseVisibleRangeScrollHandler(HasRows display, HasVerticalScrolling viewPort) {
        this.display = display;
        this.viewPort = viewPort;
        this.increment = DEFAULT_INCREMENT;
        this.scrollPositionOffset = DEFAULT_SCROLL_POSITION_OFFSET;
    }
    
    public IncreaseVisibleRangeScrollHandler(HasRows display, HasVerticalScrolling viewPort, int increment, int scrollPositionOffset) {
        this.display = display;
        this.viewPort = viewPort;
        this.increment = increment;
        this.scrollPositionOffset = scrollPositionOffset;
    }
    
    private void testIncreaseSize() {
        // If scrolling up, ignore the event
        int oldScrollPos = lastScrollPos;
        lastScrollPos = viewPort.getVerticalScrollPosition();
        if (oldScrollPos >= lastScrollPos) {
            return;
        }
        
        int maxScrollTop = viewPort.getMaximumVerticalScrollPosition();
        if (lastScrollPos >= maxScrollTop - scrollPositionOffset) {
            //we are near the end, increase the page size
            int newPageSize = Math.min(
                display.getVisibleRange().getLength() + increment, 
                display.getRowCount()
            );
            display.setVisibleRange(0, newPageSize);
        }
    }
 
    @Override
    public void onScroll(ScrollEvent event) {
        testIncreaseSize();
    }

    @Override
    public void onWindowScroll(com.google.gwt.user.client.Window.ScrollEvent event) {
        testIncreaseSize();
    }
}
