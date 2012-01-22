package com.cee.news.client.content;

import com.cee.news.client.list.EntityKeyCell;
import com.cee.news.client.list.EntityKeyProvider;
import com.cee.news.client.list.ScrollLoader;
import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;

public class SourceSelectionPanel extends Composite implements SourceSelectionView {
    private final Button selectAllButton;
    private final Button selectNoneButton;
    private final Button addSourceButton;
    private final CellList<EntityKey> cellListSites;

    public SourceSelectionPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("213px", "426px");
        
        cellListSites = new CellList<EntityKey>(new EntityKeyCell(), new EntityKeyProvider());
        ScrollLoader scrollLoader = new ScrollLoader();
        scrollLoader.setDisplay(cellListSites);
        layoutPanel.add(scrollLoader);
        layoutPanel.setWidgetLeftRight(scrollLoader, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(scrollLoader, 0.0, Unit.PX, 78.0, Unit.PX);
        
        selectAllButton = new Button("Select All");
        layoutPanel.add(selectAllButton);
        layoutPanel.setWidgetLeftWidth(selectAllButton, 10.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(selectAllButton, 44.0, Unit.PX, 24.0, Unit.PX);
        
        selectNoneButton = new Button("Select None");
        layoutPanel.add(selectNoneButton);
        layoutPanel.setWidgetLeftWidth(selectNoneButton, 94.0, Unit.PX, 97.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(selectNoneButton, 44.0, Unit.PX, 24.0, Unit.PX);
        
        addSourceButton = new Button("Add Source");
        layoutPanel.add(addSourceButton);
        layoutPanel.setWidgetLeftWidth(addSourceButton, 10.0, Unit.PX, 97.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(addSourceButton, 10.0, Unit.PX, 24.0, Unit.PX);
    }

    @Override
    public CellList<EntityKey> getCellListSites() {
        return cellListSites;
    }

    @Override
    public HasClickHandlers getSelectAllButton() {
        return selectAllButton;
    }

    @Override
    public HasClickHandlers getSelectNoneButton() {
        return selectNoneButton;
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addSourceButton;
    }
}
