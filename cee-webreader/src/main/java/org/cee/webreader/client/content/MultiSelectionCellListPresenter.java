package org.cee.webreader.client.content;

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


import org.cee.client.EntityContent;
import org.cee.webreader.client.list.CellListContentModel;
import org.cee.webreader.client.list.CellListPresenter;
import org.cee.webreader.client.list.MultiSelectListModel;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class MultiSelectionCellListPresenter<K> extends CellListPresenter<K> {

    private MultiSelectionModel<EntityContent<K>> selectionModel;

    public MultiSelectionCellListPresenter(CellList<EntityContent<K>> cellList, MultiSelectListModel<K> listModel, CellListContentModel<K> contentModel, int visibleRange) {
        super(cellList, listModel, contentModel, visibleRange);
        initSelectionModel(cellList, listModel);
    }

    public MultiSelectionCellListPresenter(CellList<EntityContent<K>> cellList, MultiSelectListModel<K> listModel, CellListContentModel<K> contentModel) {
        super(cellList, listModel, contentModel);
        initSelectionModel(cellList, listModel);
    }

    private void initSelectionModel(final CellList<EntityContent<K>> cellList,final MultiSelectListModel<K> listModel) {
        selectionModel = new MultiSelectionModel<EntityContent<K>>();
        cellList.setSelectionModel(selectionModel, new SelectionEventManager<EntityContent<K>>());
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                listModel.setSelections(EntityContent.getKeys(selectionModel.getSelectedSet()));
            }
        }); 
    }
    
    public MultiSelectionModel<EntityContent<K>> getSelectionModel() {
        return selectionModel;
    }
    
    private class SelectionEventManager<T> extends DefaultSelectionEventManager<T> {

        public SelectionEventManager() {
            super(null);
        }
        
        protected void selectOne(MultiSelectionModel<? super T> selectionModel, T target, boolean selected, boolean clearOthers) {
            //always toggle selection for simple click
            selectionModel.setSelected(target, !selectionModel.isSelected(target));
        };
    }
}
