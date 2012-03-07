package com.cee.news.client.content;

import com.cee.news.client.list.MultiSelectListModel;
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
                listModel.setSelections(selectionModel.getSelectedSet());
            }
        }); 
    }
    
    public MultiSelectionModel<EntityKey> getSelectionModel() {
        return selectionModel;
    }
    
}
