package com.cee.news.client;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.paging.PagingView;
import com.cee.news.model.ArticleKey;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface NewsView extends IsWidget {

	HasText getSiteNameLabel();

	HasClickHandlers getButtonGoToStart();

	PagingView getNewsPagingView();

	CellList<EntityContent<ArticleKey>> getWhatOthersSayCellList();

	HasClickHandlers getButtonRefresh();
}