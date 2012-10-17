package com.cee.news.client.ui;

import com.cee.news.client.content.EntityContent;
import com.cee.news.client.content.EntityContentCell;
import com.cee.news.client.content.EntityKeyProvider;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.list.IncreaseVisibleRangeScrollHandler;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SourceSelection extends Composite implements SourceSelectionView {
    
    private static SourceSelectionUiBinder uiBinder = GWT.create(SourceSelectionUiBinder.class);

    interface SourceSelectionUiBinder extends UiBinder<Widget, SourceSelection> {
    }
    
    interface SourceSelectionCellListStyle extends CellList.Style {
        
        @Override
        public String cellListEvenItem();
        
        @Override
        public String cellListOddItem();
        
        @Override
        public String cellListWidget();
        
        @Override
        public String cellListKeyboardSelectedItem();
        
        @Override
        public String cellListSelectedItem();
    }
    
    interface SourceSelectionCellListResources extends CellList.Resources {
        
        public static final SourceSelectionCellListResources INSTANCE = GWT.create(SourceSelectionCellListResources.class);
        
        @Override
        @Source("SourceSelectionCellList.css")
        public SourceSelectionCellListStyle cellListStyle();
        
        @Source("selected.png")
        public DataResource iconSelected();
        
        @Source("unselected.png")
        public DataResource iconUnselected();
    }

    @UiField
    VerticalScrollPanel scrollPanel;
    
    @UiField
    Button buttonSelectAll;
    
    @UiField
    Button buttonSelectNone;
    
    @UiField
    Button buttonAddSource;
    
    @UiField(provided=true)
    CellList<EntityContent<EntityKey>> cellListSites = new CellList<EntityContent<EntityKey>>(
            new EntityContentCell<EntityKey>(), 
            SourceSelectionCellListResources.INSTANCE, 
            new EntityKeyProvider<EntityKey>());
    
    public SourceSelection() {
        initWidget(uiBinder.createAndBindUi(this));
        scrollPanel.addScrollHandler(new IncreaseVisibleRangeScrollHandler(cellListSites, scrollPanel));
    }

    @Override
    public CellList<EntityContent<EntityKey>> getCellListSites() {
        return cellListSites;
    }

    @Override
    public HasClickHandlers getSelectAllButton() {
        return buttonSelectAll;
    }

    @Override
    public HasClickHandlers getSelectNoneButton() {
        return buttonSelectNone;
    }

    @Override
    public HasClickHandlers getAddButton() {
        return buttonAddSource;
    }
    
    @Override
    public void setButtonsEnabled(boolean enabled) {
    	buttonAddSource.setEnabled(enabled);
    	buttonSelectAll.setEnabled(enabled);
    	buttonSelectNone.setEnabled(enabled);
    }

}
