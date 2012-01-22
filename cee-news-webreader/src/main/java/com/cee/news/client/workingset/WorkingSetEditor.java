package com.cee.news.client.workingset;

import java.util.List;

import com.cee.news.client.list.ContentModel;
import com.cee.news.client.list.ListPanel;
import com.cee.news.client.list.MultiSelectListModel;
import com.cee.news.client.list.MultiSelectListPresenter;
import com.cee.news.client.list.SelectionListEditor;
import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

public class WorkingSetEditor extends DialogBox implements Editor<WorkingSetData>, WorkingSetView {
    
	private InlineLabel labelErrorMessage;
	
	private TextBox newNameEditor;
	
	private SimpleEditor<String> oldNameEditor;
	
	private SimpleEditor<Boolean> isNewEditor;
	
	private SelectionListEditor<EntityKey> sitesEditor;

	private Button buttonSave;

	private Button buttonCancel;

	private Button buttonAddNewSite;

    public WorkingSetEditor(final MultiSelectListModel<EntityKey> siteListModel, final ContentModel<EntityKey> siteContentModel) {
    	setText("Edit Working Set");
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
        
        ListPanel listPanelSites = new ListPanel();
        layoutPanel.add(listPanelSites);
        layoutPanel.setWidgetLeftWidth(listPanelSites, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(listPanelSites, 65.0, Unit.PX, 86.0, Unit.PX);
        
        ListPanel listPanelSelectedSites = new ListPanel();
        layoutPanel.add(listPanelSelectedSites);
        layoutPanel.setWidgetRightWidth(listPanelSelectedSites, 0.0, Unit.PX, 50.0, Unit.PCT);
        layoutPanel.setWidgetTopBottom(listPanelSelectedSites, 65.0, Unit.PX, 86.0, Unit.PX);
        
        new MultiSelectListPresenter<EntityKey>(siteListModel, siteContentModel, listPanelSites, listPanelSelectedSites);
        
        InlineLabel nlnlblAvailableSites = new InlineLabel("Available Sites:");
        layoutPanel.add(nlnlblAvailableSites);
        layoutPanel.setWidgetLeftWidth(nlnlblAvailableSites, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblAvailableSites, 34.0, Unit.PX, 24.0, Unit.PX);
        
        InlineLabel nlnlblSelectedSites = new InlineLabel("Selected Sites:");
        layoutPanel.add(nlnlblSelectedSites);
        layoutPanel.setWidgetLeftWidth(nlnlblSelectedSites, 50.0, Unit.PCT, 108.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblSelectedSites, 34.0, Unit.PX, 24.0, Unit.PX);
        
        Button buttonRemoveAllSites = new Button("Remove All");
        layoutPanel.add(buttonRemoveAllSites);
        layoutPanel.setWidgetRightWidth(buttonRemoveAllSites, 0.0, Unit.PX, 130.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(buttonRemoveAllSites, 56.0, Unit.PX, 24.0, Unit.PX);
        buttonRemoveAllSites.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				siteListModel.clearSelection();
			}
		});
        
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
        
        sitesEditor = new SelectionListEditor<EntityKey>(siteListModel);
        oldNameEditor = SimpleEditor.of();
        isNewEditor = SimpleEditor.of();
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
	public HasText getErrorText() {
		return labelErrorMessage;
	}
}
