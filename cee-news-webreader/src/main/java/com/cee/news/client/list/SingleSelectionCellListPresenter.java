package com.cee.news.client.list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class SingleSelectionCellListPresenter extends CellListPresenter {

    public SingleSelectionCellListPresenter(CellList<EntityContent> cellList, ListModel listModel, EntityContentModel entityContentModel, int visibleRange) {
        super(cellList, listModel, entityContentModel, visibleRange);
        initSelectionModel(cellList, listModel);
    }

    public SingleSelectionCellListPresenter(CellList<EntityContent> cellList, ListModel listModel, EntityContentModel entityContentModel) {
        super(cellList, listModel, entityContentModel);
        initSelectionModel(cellList, listModel);
    }
    
    private void initSelectionModel(final CellList<EntityContent> cellList,final ListModel listModel) {
        final SingleSelectionModel<EntityContent> selectionModel = new SingleSelectionModel<EntityContent>();
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                listModel.userSelectedKey(selectionModel.getSelectedObject().getKey().getKey());
            }
        });
    }
}
