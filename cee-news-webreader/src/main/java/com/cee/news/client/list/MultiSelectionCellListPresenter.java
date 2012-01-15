package com.cee.news.client.list;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class MultiSelectionCellListPresenter extends CellListPresenter {

    public MultiSelectionCellListPresenter(CellList<EntityContent> cellList, MultiSelectListModel listModel, EntityContentModel entityContentModel, int visibleRange) {
        super(cellList, listModel, entityContentModel, visibleRange);
        initSelectionModel(cellList, listModel);
    }

    public MultiSelectionCellListPresenter(CellList<EntityContent> cellList, MultiSelectListModel listModel, EntityContentModel entityContentModel) {
        super(cellList, listModel, entityContentModel);
        initSelectionModel(cellList, listModel);
    }

    private void initSelectionModel(final CellList<EntityContent> cellList,final MultiSelectListModel listModel) {
        final MultiSelectionModel<EntityContent> selectionModel = new MultiSelectionModel<EntityContent>();
        cellList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Collection<String> selectedKeys = new ArrayList<String>();
                for (EntityContent content : selectionModel.getSelectedSet()) {
                    selectedKeys.add(content.getKey().getKey());
                }
                listModel.setSelections(selectedKeys);
            }
        }); 
    }
    
}
