package org.cee.webreader.client.ui;

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


import org.cee.news.model.EntityKey;
import org.cee.webreader.client.content.EntityContent;
import org.cee.webreader.client.content.EntityContentCell;
import org.cee.webreader.client.content.EntityContentKeyProvider;
import org.cee.webreader.client.content.SourceSelectionView;
import org.cee.webreader.client.list.IncreaseVisibleRangeScrollHandler;

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
            new EntityContentKeyProvider<EntityKey>());
    
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
