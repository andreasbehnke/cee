package com.cee.news.client.content;

import com.cee.news.client.list.ListModel;
import com.cee.news.model.EntityKey;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class SingleSelectionCellListPresenter extends CellListPresenter {

    public SingleSelectionCellListPresenter(CellList<EntityKey> cellList, ListModel<EntityKey> listModel, EntityKeyContentModel contentModel, int visibleRange) {
        super(cellList, listModel, contentModel, visibleRange);
        initSelectionModel(cellList, listModel);
    }

    public SingleSelectionCellListPresenter(CellList<EntityKey> cellList, ListModel<EntityKey> listModel, EntityKeyContentModel contentModel) {
        super(cellList, listModel, contentModel);
        initSelectionModel(cellList, listModel);
    }
    
    private void initSelectionModel(final CellList<EntityKey> cellList, final ListModel<EntityKey> listModel) {
        final NoSelectionModel<EntityKey> selectionModel = new NoSelectionModel<EntityKey>();
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                listModel.userSelectedKey(selectionModel.getLastSelectedObject());
            }
        });
    }
}
