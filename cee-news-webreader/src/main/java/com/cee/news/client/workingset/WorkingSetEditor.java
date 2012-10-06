package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.list.ListPanel;
import com.cee.news.client.list.ListView;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.cee.news.client.list.SelectionListEditor;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class WorkingSetEditor extends PopupPanel implements Editor<WorkingSetData>, WorkingSetView {

    private final InlineLabel labelErrorMessage;

    private final TextBox newNameEditor;

    private final SimpleEditor<String> oldNameEditor;

    private final SimpleEditor<Boolean> isNewEditor;

    private final SelectionListEditor<EntityKey> sitesEditor;

    private final Button buttonSave;

    private final Button buttonCancel;

    private final Button buttonAddNewSite;

    private final Button buttonRemoveAllSites;
    
    private final ListPanel availableSitesList;
    
    private final ListPanel selectedSitesList;
    
    private final WorkingSetDataEditorDriver driver;

    public WorkingSetEditor() {
        driver = GWT.create(WorkingSetDataEditorDriver.class);
        //set Text("Edit Working Set");
        setGlassEnabled(true);
        LayoutPanel layoutPanel = new LayoutPanel();
        setWidget(layoutPanel);
        layoutPanel.setSize("627px", "498px");

        InlineLabel nlnlblNewInlinelabel = new InlineLabel("Working Set Name:");
        layoutPanel.add(nlnlblNewInlinelabel);
        layoutPanel.setWidgetLeftWidth(nlnlblNewInlinelabel, 0.0, Unit.PX, 121.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblNewInlinelabel, 0.0, Unit.PX, 28.0, Unit.PX);

        newNameEditor = new TextBox();
        layoutPanel.add(newNameEditor);
        layoutPanel.setWidgetLeftRight(newNameEditor, 127.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(newNameEditor, 0.0, Unit.PX, 28.0, Unit.PX);

        availableSitesList = new ListPanel();
        layoutPanel.add(availableSitesList);
        layoutPanel.setWidgetLeftWidth(availableSitesList, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(availableSitesList, 65.0, Unit.PX, 86.0, Unit.PX);

        selectedSitesList = new ListPanel();
        layoutPanel.add(selectedSitesList);
        layoutPanel.setWidgetRightWidth(selectedSitesList, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(selectedSitesList, 65.0, Unit.PX, 86.0, Unit.PX);

        InlineLabel nlnlblAvailableSites = new InlineLabel("Available Sites:");
        layoutPanel.add(nlnlblAvailableSites);
        layoutPanel.setWidgetLeftWidth(nlnlblAvailableSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblAvailableSites, 34.0, Unit.PX, 24.0, Unit.PX);

        InlineLabel nlnlblSelectedSites = new InlineLabel("Selected Sites:");
        layoutPanel.add(nlnlblSelectedSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSelectedSites, 50.0, Unit.PCT, 108.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSelectedSites, 34.0, Unit.PX, 24.0, Unit.PX);

        buttonRemoveAllSites = new Button("Remove All");
        layoutPanel.add(buttonRemoveAllSites);
        layoutPanel.setWidgetRightWidth(buttonRemoveAllSites, 0.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonRemoveAllSites, 56.0, Unit.PX, 24.0, Unit.PX);
        
        buttonAddNewSite = new Button("Add New Site");
        layoutPanel.add(buttonAddNewSite);
        layoutPanel.setWidgetRightWidth(buttonAddNewSite, 50.0, Unit.PCT, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonAddNewSite, 56.0, Unit.PX, 24.0, Unit.PX);

        buttonSave = new Button("Save");
        layoutPanel.add(buttonSave);
        layoutPanel.setWidgetRightWidth(buttonSave, 0.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonSave, 0.0, Unit.PX, 24.0, Unit.PX);

        buttonCancel = new Button("Cancel");
        buttonCancel.setText("Cancel");
        layoutPanel.add(buttonCancel);
        layoutPanel.setWidgetRightWidth(buttonCancel, 136.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonCancel, 0.0, Unit.PX, 24.0, Unit.PX);

        labelErrorMessage = new InlineLabel("");
        layoutPanel.add(labelErrorMessage);
        layoutPanel.setWidgetLeftRight(labelErrorMessage, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(labelErrorMessage, 31.0, Unit.PX, 19.0, Unit.PX);

        sitesEditor = new SelectionListEditor<EntityKey>();
        oldNameEditor = SimpleEditor.of();
        isNewEditor = SimpleEditor.of();

        driver.initialize(this);
    }

    @Override
    public void edit(WorkingSetData wsd) {
        driver.edit(wsd);
    }

    @Override
    public WorkingSetData getData() {
        return driver.flush();
    }

    @Override
    public boolean hasValidationErrors() {
        return driver.hasErrors();
    }

    @Override
    public void showValidationErrors() {
        List<EditorError> errors = driver.getErrors();
        String message = "";
        for (EditorError editorError : errors) {
            message += editorError.getMessage();
        }
        labelErrorMessage.setText(message);
    }

    @Override
    public HasText getErrorText() {
        return labelErrorMessage;
    }

    @Override
    public HasClickHandlers getButtonSave() {
        return buttonSave;
    }

    @Override
    public HasClickHandlers getButtonCancel() {
        return buttonCancel;
    }

    @Override
    public HasClickHandlers getButtonAddNewSite() {
        return buttonAddNewSite;
    }
    
    @Override
    public HasClickHandlers getButtonRemoveAllSites() {
        return buttonRemoveAllSites;
    }
    
    @Override
    public ListView getAvailableSitesList() {
        return availableSitesList;
    }
    
    @Override
    public ListView getSelectedSitesList() {
        return selectedSitesList;
    }
    
    @Override
    public void addSelectionListChangedHandler(SelectionListChangedHandler<EntityKey> selectionListChangedHandler) {
        sitesEditor.addSelectionListChangedHandler(selectionListChangedHandler);
    }
    
    @Override
    public void setSelectedSites(List<EntityKey> selectedSites) {
        sitesEditor.setSelections(selectedSites);
    }
    
    public IsEditor<ValueBoxEditor<String>> newName() {
        return newNameEditor;
    }

    public SimpleEditor<String> oldName() {
        return oldNameEditor;
    }

    public SimpleEditor<Boolean> isNew() {
        return isNewEditor;
    }

    public Editor<List<EntityKey>> sites() {
        return sitesEditor;
    }
}
