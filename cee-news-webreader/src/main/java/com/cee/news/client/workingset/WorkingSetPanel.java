package com.cee.news.client.workingset;

import com.cee.news.client.list.ListPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

public class WorkingSetPanel extends Composite {
    private InlineLabel labelValidationMessage;

    public WorkingSetPanel() {
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("593px", "372px");
        
        InlineLabel nlnlblNewInlinelabel = new InlineLabel("Working Set Name:");
        layoutPanel.add(nlnlblNewInlinelabel);
        layoutPanel.setWidgetLeftWidth(nlnlblNewInlinelabel, 0.0, Unit.PX, 121.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblNewInlinelabel, 0.0, Unit.PX, 28.0, Unit.PX);
        
        TextBox textBoxName = new TextBox();
        layoutPanel.add(textBoxName);
        layoutPanel.setWidgetLeftRight(textBoxName, 127.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(textBoxName, 0.0, Unit.PX, 28.0, Unit.PX);
        
        ListPanel listPanelSites = new ListPanel();
        layoutPanel.add(listPanelSites);
        layoutPanel.setWidgetLeftWidth(listPanelSites, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(listPanelSites, 64.0, Unit.PX, 86.0, Unit.PX);
        
        InlineLabel nlnlblAvailableSites = new InlineLabel("Available Sites:");
        layoutPanel.add(nlnlblAvailableSites);
        layoutPanel.setWidgetLeftWidth(nlnlblAvailableSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblAvailableSites, 34.0, Unit.PX, 24.0, Unit.PX);
        
        ListPanel listPanelSelectedSites = new ListPanel();
        layoutPanel.add(listPanelSelectedSites);
        layoutPanel.setWidgetRightWidth(listPanelSelectedSites, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(listPanelSelectedSites, 65.0, Unit.PX, 86.0, Unit.PX);
        
        InlineLabel nlnlblSelectedSites = new InlineLabel("Selected Sites:");
        layoutPanel.add(nlnlblSelectedSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSelectedSites, 50.0, Unit.PCT, 108.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSelectedSites, 34.0, Unit.PX, 24.0, Unit.PX);
        
        Button buttonRemoveAll = new Button("Remove All");
        layoutPanel.add(buttonRemoveAll);
        layoutPanel.setWidgetRightWidth(buttonRemoveAll, 0.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonRemoveAll, 292.0, Unit.PX, 24.0, Unit.PX);
        
        Button buttonAddNewSite = new Button("Add New Site");
        layoutPanel.add(buttonAddNewSite);
        layoutPanel.setWidgetRightWidth(buttonAddNewSite, 50.0, Unit.PCT, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonAddNewSite, 56.0, Unit.PX, 24.0, Unit.PX);
        
        Button buttonSave = new Button("Save");
        layoutPanel.add(buttonSave);
        layoutPanel.setWidgetRightWidth(buttonSave, 0.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonSave, 0.0, Unit.PX, 24.0, Unit.PX);
        
        Button buttonCancel = new Button("Cancel");
        buttonCancel.setText("Cancel");
        layoutPanel.add(buttonCancel);
        layoutPanel.setWidgetLeftWidth(buttonCancel, 327.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonCancel, 348.0, Unit.PX, 24.0, Unit.PX);
        
        labelValidationMessage = new InlineLabel("");
        layoutPanel.add(labelValidationMessage);
        layoutPanel.setWidgetLeftRight(labelValidationMessage, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(labelValidationMessage, 30.0, Unit.PX, 19.0, Unit.PX);
    }
}
