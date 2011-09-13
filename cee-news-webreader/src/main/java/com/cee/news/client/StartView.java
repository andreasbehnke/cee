package com.cee.news.client;

import com.cee.news.client.list.ListView;
import com.cee.news.client.progress.ProgressView;
import com.cee.news.client.workingset.WorkingSetSelectionView;

public interface StartView {

	ListView getLatestArticlesListView();

	WorkingSetSelectionView getWorkingSetSelectionView();

	ListView getSitesListView();

	ProgressView getProgressView();

}