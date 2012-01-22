package com.cee.news.client;

import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.cellview.client.CellList;

public interface StartView {

	CellList<EntityKey> getCellListLatestArticles();

	WorkingSetSelectionView getWorkingSetSelectionView();

	SourceSelectionView getSourceSelectionView();

	ProgressView getProgressView();

}