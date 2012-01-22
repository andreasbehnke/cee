package com.cee.news.client;

import com.cee.news.client.paging.PagingView;
import com.cee.news.model.EntityKey;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasText;

public interface NewsView {

	public HasText getSiteNameLabel();

	public HasClickHandlers getButtonGoToStart();

	public PagingView getPagingView();

	public CellList<EntityKey> getWhatOthersSayCellList();

}