package com.cee.news.client;

import com.cee.news.client.list.ListView;
import com.cee.news.client.workingset.WorkingSetSelectionView;

public interface StartView {

	public abstract ListView getLatestArticlesListView();

	public abstract WorkingSetSelectionView getWorkingSetSelectionView();

	public abstract ListView getSitesListView();

}