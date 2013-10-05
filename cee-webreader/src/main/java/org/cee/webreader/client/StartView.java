package org.cee.webreader.client;

import org.cee.news.model.ArticleKey;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.content.SourceSelectionView;
import org.cee.webreader.client.search.SearchView;
import org.cee.webreader.client.workingset.WorkingSetSelectionView;

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