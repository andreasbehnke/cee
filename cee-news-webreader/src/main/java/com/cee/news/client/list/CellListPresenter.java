package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.error.ErrorSourceBase;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public abstract class CellListPresenter extends ErrorSourceBase {
    
    private static final int DEFAULT_VISIBLE_RANGE = 20;
    
    private List<EntityKey> keys;
	
	private int offset;
	
	public CellListPresenter(final CellList<EntityKey> cellList, final ListModel<EntityKey> listModel, final EntityKeyContentModel contentModel) {
	    this(cellList, listModel, contentModel, DEFAULT_VISIBLE_RANGE);
	}
	
	public CellListPresenter(final CellList<EntityKey> cellList, final ListModel<EntityKey> listModel, final EntityKeyContentModel contentModel, int visibleRange) {
		listModel.addListChangedHandler(new ListChangedHandler<EntityKey>() {
			@Override
			public void onContentListChanged(ListChangedEvent<EntityKey> event) {
				offset = 0;
				keys = event.getValues();
				int count = keys.size();
				int rangeLength = (count < 20) ? count : 20;
				cellList.setRowCount(count, true);
				cellList.setVisibleRangeAndClearData(new Range(0, rangeLength), true);
			}
		});
		
		final AsyncDataProvider<EntityKey> dataProvider = new AsyncDataProvider<EntityKey>() {
			@Override
			protected void onRangeChanged(HasData<EntityKey> display) {
				if (keys == null) {
					return;
				}
				final Range range = display.getVisibleRange();
				final int end = range.getLength();
				ArrayList<EntityKey> keysInRange = new ArrayList<EntityKey>(keys.subList(offset, end));
				contentModel.getContent(keysInRange, new AsyncCallback<List<EntityKey>>() {
					
					@Override
					public void onSuccess(List<EntityKey> result) {
						cellList.setRowData(offset, result);
						offset = end;
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
