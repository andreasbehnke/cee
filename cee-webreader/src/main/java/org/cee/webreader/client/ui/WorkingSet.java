package org.cee.webreader.client.ui;

import java.util.List;

import org.cee.news.model.EntityKey;
import org.cee.webreader.client.content.EntityKeyProvider;
import org.cee.webreader.client.content.EntityKeyRenderer;
import org.cee.webreader.client.list.ListPanel;
import org.cee.webreader.client.list.ListView;
import org.cee.webreader.client.list.SelectionListChangedHandler;
import org.cee.webreader.client.list.SelectionListEditor;
import org.cee.webreader.client.workingset.WorkingSetData;
import org.cee.webreader.client.workingset.WorkingSetView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.SimpleEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class WorkingSet extends PopupPanel implements WorkingSetView {

    public interface WorkingSetUiBinder extends UiBinder<Widget, WorkingSet> {}
    
    public class WorkingSetEditor implements Editor<WorkingSetData> {
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
        
        public IsEditor<TakesValueEditor<EntityKey>> language() {
        	return listBoxLanguage;
        }
    }
    
    private static WorkingSetUiBinder uiBinder = GWT.create(WorkingSetUiBinder.class);
    
    private static Resources resources = GWT.create(Resources.class);
    
    @UiField
    InlineLabel labelErrorMessage;

    @UiField
    TextBox newNameEditor;
    
    @UiField(provided = true)
    ValueListBox<EntityKey> listBoxLanguage;

    private final SimpleEditor<String> oldNameEditor;

    private final SimpleEditor<Boolean> isNewEditor;

    private final SelectionListEditor<EntityKey> sitesEditor;

    @UiField
    Button buttonSave;

    @UiField
    Button buttonCancel;

    @UiField
    Button buttonAddNewSite;

    @UiField
    Button buttonRemoveAllSites;
    
    @UiField
    ListPanel availableSitesList;
    
    @UiField
    ListPanel selectedSitesList;
    
    private final WorkingSetDataEditorDriver driver;

    public WorkingSet() {
    	listBoxLanguage = new ValueListBox<EntityKey>(new EntityKeyRenderer(), new EntityKeyProvider());
    	
        setWidget(uiBinder.createAndBindUi(this));
        setGlassEnabled(true);
        setStyleName(resources.styles().popupPanel());
        
        sitesEditor = new SelectionListEditor<EntityKey>();
        oldNameEditor = SimpleEditor.of();
        isNewEditor = SimpleEditor.of();
        driver = GWT.create(WorkingSetDataEditorDriver.class);
        driver.initialize(new WorkingSet.WorkingSetEditor());
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
    public void addSiteSelectionListChangedHandler(SelectionListChangedHandler<EntityKey> selectionListChangedHandler) {
        sitesEditor.addSelectionListChangedHandler(selectionListChangedHandler);
    }
    
    @Override
    public void setSelectedSites(List<EntityKey> selectedSites) {
        sitesEditor.setSelections(selectedSites);
    }

	@Override
	public void setAvailableLanguages(List<EntityKey> languages, EntityKey defaultLanguage) {
		listBoxLanguage.setValue(defaultLanguage);
		listBoxLanguage.setAcceptableValues(languages);
	}
	
	@Override
	public void addLanguageChangedHandler(ValueChangeHandler<EntityKey> handler) {
		listBoxLanguage.addValueChangeHandler(handler);
	}
}
