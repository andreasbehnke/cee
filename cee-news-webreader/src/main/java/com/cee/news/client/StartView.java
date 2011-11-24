package com.cee.news.client;

import com.cee.news.client.list.EntityContent;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.google.gwt.user.cellview.client.CellList;

public interface StartView {

	CellList<EntityContent> getCellListLatestArticles();

	WorkingSetSelectionView getWorkingSetSelectionView();

	CellList<EntityContent> getCellListSites();

	ProgressView getProgressView();

}