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
	
	public CellListPresenter(final CellList<EntityContent> cellList, final ListModel listModel, final EntityContentModel entityContentModel) {
	    this(cellList, listModel, entityContentModel, DEFAULT_VISIBLE_RANGE);
	}
	
	public CellListPresenter(final CellList<EntityContent> cellList, final ListModel listModel, final EntityContentModel entityContentModel, int visibleRange) {
		listModel.addListChangedHandler(new ListChangedHandler() {
			@Override
			public void onContentListChanged(ListChangedEvent event) {
				offset = 0;
				keys = event.getLinks();
				int count = keys.size();
				int rangeLength = (count < 20) ? count : 20;
				cellList.setRowCount(count, true);
				cellList.setVisibleRangeAndClearData(new Range(0, rangeLength), true);
			}
		});
		
		final AsyncDataProvider<EntityContent> dataProvider = new AsyncDataProvider<EntityContent>() {
			@Override
			protected void onRangeChanged(HasData<EntityContent> display) {
				if (keys == null) {
					return;
				}
				final Range range = display.getVisibleRange();
				final int end = range.getLength();
				ArrayList<EntityKey> keysInRange = new ArrayList<EntityKey>(keys.subList(offset, end));
				entityContentModel.getContent(keysInRange, new AsyncCallback<List<EntityContent>>() {
					
					@Override
					public void onSuccess(List<EntityContent> result) {
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
