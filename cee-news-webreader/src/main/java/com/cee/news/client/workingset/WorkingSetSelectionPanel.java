package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;

public class WorkingSetSelectionPanel extends Composite implements WorkingSetSelectionView {
    
    private Button buttonNew;
    
    private Button buttonEdit;
    
    private ListBox comboBoxSelect;
    
    public WorkingSetSelectionPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("581px", "24px");
        
        Label labelSelect = new Label("Select Working Set:");
        labelSelect.setWordWrap(false);
        layoutPanel.add(labelSelect);
        layoutPanel.setWidgetLeftWidth(labelSelect, 0.0, Unit.PX, 128.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(labelSelect, 0.0, Unit.PX, 24.0, Unit.PX);
        
        buttonNew = new Button("New button");
        buttonNew.setText("new");
        layoutPanel.add(buttonNew);
        layoutPanel.setWidgetRightWidth(buttonNew, 0.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonNew, 0.0, Unit.PX, 24.0, Unit.PX);
        
        comboBoxSelect = new ListBox();
        layoutPanel.add(comboBoxSelect);
        layoutPanel.setWidgetLeftRight(comboBoxSelect, 134.0, Unit.PX, 168.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(comboBoxSelect, 0.0, Unit.PX, 24.0, Unit.PX);
        
        buttonEdit = new Button("New button");
        buttonEdit.setText("edit");
        layoutPanel.add(buttonEdit);
        layoutPanel.setWidgetRightWidth(buttonEdit, 84.0, Unit.PX, 78.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonEdit, 0.0, Unit.PX, 24.0, Unit.PX);
    }
    
    public int getSelectedWorkingSet() {
        return comboBoxSelect.getSelectedIndex();
    }
    
    public void setSelectedWorkingSet(int index) {
        comboBoxSelect.setSelectedIndex(index);
    }

    public void addSelectionChangedHandler(ChangeHandler handler) {
        comboBoxSelect.addChangeHandler(handler);
    }

    public void setWorkingSets(List<EntityKey> names) {
        comboBoxSelect.clear();
        for (EntityKey name : names) {
            comboBoxSelect.addItem(name.getName());
        }
    }

    public HasClickHandlers getNewButton() {
        return buttonNew;
    }

    public HasClickHandlers getEditButton() {
        return buttonEdit;
    }
}
