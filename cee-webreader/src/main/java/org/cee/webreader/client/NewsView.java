package org.cee.webreader.client;

import org.cee.news.model.ArticleKey;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.paging.PagingView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface NewsView extends IsWidget {

	HasText getSiteNameLabel();

	HasClickHandlers getButtonGoToStart();

	PagingView getNewsPagingView();

	CellList<EntityContent<ArticleKey>> getWhatOthersSayCellList();
	
	int getNumberOfVisibleRelatedArticles();

	HasClickHandlers getButtonRefresh();
	
    void registerScrollHandler();
    
    void removeScrollHandler();

}