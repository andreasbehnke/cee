package com.cee.news.client.list;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;

public class ScrollLoader extends Composite {
	
	private static final int DEFAULT_INCREMENT = 20;
	
	private int incrementSize = DEFAULT_INCREMENT;
	
	private int lastScrollPos = 0;
	
	private final ScrollPanel scrollPanel = new ScrollPanel();

	private HasRows display;
	
	public ScrollLoader() {
		initWidget(scrollPanel);
		scrollPanel.getElement().setTabIndex(-1);
		scrollPanel.addScrollHandler(new ScrollHandler() {
			
			@Override
			public void onScroll(ScrollEvent event) {
				// If scrolling up, ignore the event
				int oldScrollPos = lastScrollPos;
				lastScrollPos = scrollPanel.getVerticalScrollPosition();
				if (oldScrollPos >= lastScrollPos) {
					return;
				}
				
				if (display == null) {
					return;
				}
				int maxScrollTop = scrollPanel.getWidget().getOffsetHeight()
					- scrollPanel.getOffsetHeight();
				if (lastScrollPos >= maxScrollTop) {
					//we are near the end, increase the page size
					int newPageSize = Math.min(
						display.getVisibleRange().getLength() + incrementSize, 
						display.getRowCount()
					);
					display.setVisibleRange(0, newPageSize);
				}
			}
		});
	}

	public int getIncrementSize() {
		return incrementSize;
	}

	public void setIncrementSize(int incrementSize) {
		this.incrementSize = incrementSize;
	}
	
	public void setDisplay(HasRows display) {
		this.display = display;
		scrollPanel.setWidget((Widget)display);
	}
}
