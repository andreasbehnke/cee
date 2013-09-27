package com.cee.news.client.content;

import com.cee.news.client.list.CellListContentModel;
import com.cee.news.client.list.CellListPresenter;
import com.cee.news.client.list.MultiSelectListModel;
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
