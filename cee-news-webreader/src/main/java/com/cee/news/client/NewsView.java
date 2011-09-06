package com.cee.news.client;

import com.cee.news.client.list.ListView;
import com.cee.news.client.paging.PagingView;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface NewsView {

	public abstract HasClickHandlers getButtonGoToStart();

	public abstract PagingView getPagingView();

	public abstract ListView getWhatOthersSayListView();

}