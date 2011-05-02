package com.cee.news.client;

import com.cee.news.client.content.NewsPagingContentModel;
import com.cee.news.client.content.SiteAddRemoveListModel;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.paging.PagingPanel;
import com.cee.news.client.paging.PagingPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.cee.news.client.list.AddRemoveListPresenter;
import com.cee.news.client.list.ListModel;
import com.cee.news.client.list.ListPanel;
import com.cee.news.client.list.ListPresenter;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.google.gwt.user.client.ui.DeckPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootPanel.add(layoutPanel);
		
		DeckPanel deckPanel = new DeckPanel();
		layoutPanel.add(deckPanel);
		layoutPanel.setWidgetTopBottom(deckPanel, 0.0, Unit.PX, -468.0, Unit.PX);
		
		StartPanel startPanel = new StartPanel();
		deckPanel.add(startPanel);
		startPanel.setSize("100%", "100%");
		
		NewsPanel newsPanel = new NewsPanel();
		deckPanel.add(newsPanel);
		newsPanel.setSize("100%", "100%");
		deckPanel.showWidget(0);
		
		new WorkingSetSelectionPresenter(null, startPanel.getWorkingSetSelectionPanel());
		
	}   
}
