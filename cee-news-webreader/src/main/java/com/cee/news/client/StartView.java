package com.cee.news.client;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.search.SearchView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.ArticleKey;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.IsWidget;

public interface StartView extends IsWidget {

	CellList<EntityContent<ArticleKey>> getCellListLatestArticles();

	WorkingSetSelectionView getWorkingSetSelectionView();

	SourceSelectionView getSourceSelectionView();
	
	SearchView getSearchView();
	
	HasClickHandlers getButtonRefresh();
	
	int getNumberOfVisibleArticleTeasers();
	
	void registerScrollHandler();
	
	void removeScrollHandler();
}