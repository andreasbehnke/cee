package com.cee.news.client.content;

import com.cee.news.client.list.IncreaseVisibleRangeScrollHandler;
import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class SourceSelectionPanel extends Composite implements SourceSelectionView {
    private final Button selectAllButton;
    private final Button selectNoneButton;
    private final Button addSourceButton;
    private final CellList<EntityContent<EntityKey>> cellListSites;

    public SourceSelectionPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("213px", "426px");
        
        cellListSites = new CellList<EntityContent<EntityKey>>(new EntityContentCell<EntityKey>(), new EntityKeyProvider<EntityKey>());
        ScrollPanel scrollPanel = new ScrollPanel(cellListSites);
        scrollPanel.addScrollHandler(new IncreaseVisibleRangeScrollHandler(cellListSites, scrollPanel));
        layoutPanel.add(scrollPanel);
        layoutPanel.setWidgetLeftRight(scrollPanel, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(scrollPanel, 0.0, Unit.PX, 78.0, Unit.PX);
        
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
    public CellList<EntityContent<EntityKey>> getCellListSites() {
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
