package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.Collection;

import com.cee.news.model.EntityKey;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class MultiSelectionCellListPresenter extends CellListPresenter {

    private MultiSelectionModel<EntityKey> selectionModel;

    public MultiSelectionCellListPresenter(CellList<EntityKey> cellList, MultiSelectListModel<EntityKey> listModel, EntityKeyContentModel contentModel, int visibleRange) {
        super(cellList, listModel, contentModel, visibleRange);
        initSelectionModel(cellList, listModel);
    }

    public MultiSelectionCellListPresenter(CellList<EntityKey> cellList, MultiSelectListModel<EntityKey> listModel, EntityKeyContentModel contentModel) {
        super(cellList, listModel, contentModel);
        initSelectionModel(cellList, listModel);
    }

    private void initSelectionModel(final CellList<EntityKey> cellList,final MultiSelectListModel<EntityKey> listModel) {
        selectionModel = new MultiSelectionModel<EntityKey>();
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Collection<EntityKey> selectedKeys = new ArrayList<EntityKey>();
                for (EntityKey key : selectionModel.getSelectedSet()) {
                    selectedKeys.add(key);
                }
                listModel.setSelections(selectedKeys);
            }
        }); 
    }
    
    public MultiSelectionModel<EntityKey> getSelectionModel() {
        return selectionModel;
    }
    
}
