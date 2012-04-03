package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.error.ErrorSourceBase;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public abstract class CellListPresenter<K> extends ErrorSourceBase {
    
    private static final int DEFAULT_VISIBLE_RANGE = 20;
    
    private List<K> keys;
	
	private int offset;
	
	public CellListPresenter(final CellList<EntityContent<K>> cellList, final ListModel<K> listModel, final CellListContentModel<K> contentModel) {
	    this(cellList, listModel, contentModel, DEFAULT_VISIBLE_RANGE);
	}
	
	public CellListPresenter(final CellList<EntityContent<K>> cellList, final ListModel<K> listModel, final CellListContentModel<K> contentModel, int visibleRange) {
		listModel.addListChangedHandler(new ListChangedHandler<K>() {
			@Override
			public void onContentListChanged(ListChangedEvent<K> event) {
				offset = 0;
				keys = event.getValues();
				int count = keys.size();
				int rangeLength = (count < 20) ? count : 20;
				cellList.setRowCount(count, true);
				cellList.setVisibleRangeAndClearData(new Range(0, rangeLength), true);
			}
		});
		
		final AsyncDataProvider<EntityContent<K>> dataProvider = new AsyncDataProvider<EntityContent<K>>() {
			@Override
			protected void onRangeChanged(HasData<EntityContent<K>> display) {
				if (keys == null) {
					return;
				}
				final Range range = display.getVisibleRange();
				final int end = range.getLength();
				final int size = keys.size();
				if (end > size || offset >= size) {
				    return;
				}
				ArrayList<K> keysInRange = new ArrayList<K>(keys.subList(offset, end));
				contentModel.getContent(keysInRange, new AsyncCallback<List<EntityContent<K>>>() {
					
					@Override
					public void onSuccess(List<EntityContent<K>> result) {
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
