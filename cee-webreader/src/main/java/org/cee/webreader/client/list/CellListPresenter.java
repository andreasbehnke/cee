package org.cee.webreader.client.list;

/*
 * #%L
 * News Reader
 * %%
 * Copyright (C) 2013 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.error.ErrorSourceBase;

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
	
	public CellListPresenter(final CellList<EntityContent<K>> cellList, final ListModel<K> listModel, final CellListContentModel<K> contentModel, final int visibleRange) {
		listModel.addListChangedHandler(new ListChangedHandler<K>() {
			@Override
			public void onContentListChanged(ListChangedEvent<K> event) {
				offset = 0;
				keys = event.getValues();
				int count = keys.size();
				int rangeLength = (count < visibleRange) ? count : visibleRange;
				cellList.setRowCount(count, true);
				cellList.setVisibleRangeAndClearData(new Range(0, rangeLength), true);
			}
		});
		
		final AsyncDataProvider<EntityContent<K>> dataProvider = new AsyncDataProvider<EntityContent<K>>() {
		    
		    private boolean retrieving = false;
		    
			@Override
			protected void onRangeChanged(HasData<EntityContent<K>> display) {
				if (keys == null) {
					return;
				}
				if (retrieving) {
				    return;
				}
				final Range range = display.getVisibleRange();
				final int end = range.getLength();
				final int size = keys.size();
				if (end > size || offset >= size) {
				    return;
				}
				ArrayList<K> keysInRange = new ArrayList<K>(keys.subList(offset, end));
				retrieving = true;
				contentModel.getContent(keysInRange, new AsyncCallback<List<EntityContent<K>>>() {
					
					@Override
					public void onSuccess(List<EntityContent<K>> result) {
					    cellList.setRowData(offset, result);
						offset = end;
						retrieving = false;
					}
					
					@Override
					public void onFailure(Throwable caught) {
					    retrieving = false;
						fireErrorEvent(caught, "Could not retrieve new data for cell list");
					}
				});
			}
		};
		dataProvider.addDataDisplay(cellList);
	}
}
