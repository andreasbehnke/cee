package com.cee.news.client.list;

import java.util.List;

import com.cee.news.client.error.ErrorSourceBase;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class CellListPresenter extends ErrorSourceBase {
	
	private List<EntityKey> keys;

	public CellListPresenter(final CellList<EntityContent> cellList, final ListModel listModel, final EntityContentModel entityContentModel) {
		listModel.addListChangedHandler(new ListChangedHandler() {
			@Override
			public void onContentListChanged(ListChangedEvent event) {
				keys = event.getLinks();
				cellList.setRowCount(keys.size(), true);
			}
		});
		
		final AsyncDataProvider<EntityContent> dataProvider = new AsyncDataProvider<EntityContent>() {
			@Override
			protected void onRangeChanged(HasData<EntityContent> display) {
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				final int end = start + range.getLength();
				List<EntityKey> keysInRange = keys.subList(start, end);
				entityContentModel.getContent(keysInRange, new AsyncCallback<List<EntityContent>>() {
					
					@Override
					public void onSuccess(List<EntityContent> result) {
						cellList.setRowData(start, result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						fireErrorEvent(caught, "Could not retrieve new data for cell list");
					}
				});
			}
		};
		
		dataProvider.addDataDisplay(cellList);
	}
}
