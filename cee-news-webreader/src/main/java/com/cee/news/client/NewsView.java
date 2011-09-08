package com.cee.news.client;

import com.cee.news.client.list.ListView;
import com.cee.news.client.paging.PagingView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;

public interface NewsView {

	public HasText getSiteNameLabel();

	public HasClickHandlers getButtonGoToStart();

	public PagingView getPagingView();

	public ListView getWhatOthersSayListView();

}