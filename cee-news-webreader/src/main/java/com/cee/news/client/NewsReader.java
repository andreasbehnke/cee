package com.cee.news.client;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.AddSiteWorkflow;
import com.cee.news.client.content.NewSiteWizard;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.content.SiteAddRemoveListModel;
import com.cee.news.client.content.SiteAddedEvent;
import com.cee.news.client.content.SiteAddedHandler;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.list.ListPresenter;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.workingset.NewWorkingSetWorkflow;
import com.cee.news.client.workingset.WorkingSetEditor;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		ErrorDialog errorDialog = new ErrorDialog();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootPanel.add(layoutPanel);
		
		DeckPanel deckPanel = new DeckPanel();
		layoutPanel.add(deckPanel);
		layoutPanel.setWidgetTopBottom(deckPanel, 0.0, Unit.PX, -468.0, Unit.PX);
		
		StartPanel startPanel = new StartPanel();
		deckPanel.add(startPanel);
		startPanel.setSize("100%", "100%");
		
		NewsPanel newsPanel = new NewsPanel();
		deckPanel.add(newsPanel);
		newsPanel.setSize("100%", "100%");
		deckPanel.showWidget(0);
		
		//Working set selection
		final WorkingSetListModel workingSetListModel = new WorkingSetListModel();
		workingSetListModel.addErrorHandler(errorDialog);
		final WorkingSetSelectionView workingSetSelectionView = startPanel.getWorkingSetSelectionPanel();
		final SiteAddRemoveListModel siteAddRemoveListModel = new SiteAddRemoveListModel();
		siteAddRemoveListModel.addErrorHandler(errorDialog);
		
		//Site List
		final SiteListContentModel sitesOfWorkingSetModel = new SiteListContentModel();
		sitesOfWorkingSetModel.addErrorHandler(errorDialog);
		new ListPresenter(sitesOfWorkingSetModel, startPanel.getListViewSites());
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChange(SelectionChangedEvent event) {
				sitesOfWorkingSetModel.updateSites(workingSetListModel.getWorkingSetName(event.getSelection()));
			}
		});
		
		//New Working Set Workflow
		final WorkingSetEditor workingSetEditor = new WorkingSetEditor(siteAddRemoveListModel);
		final NewWorkingSetWorkflow newWorkingSetWorkflow = new NewWorkingSetWorkflow(workingSetListModel, workingSetEditor);
		newWorkingSetWorkflow.addErrorHandler(errorDialog);
		
		new WorkingSetSelectionPresenter(workingSetListModel, workingSetSelectionView);
		workingSetSelectionView.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				newWorkingSetWorkflow.start();
			}
		});
		
		final NewSiteWizardView newSiteWizard = new NewSiteWizard();
		
		//Add Site Workflow
		final AddSiteWorkflow addSiteWorkflow = new AddSiteWorkflow(newSiteWizard);
		addSiteWorkflow.addErrorHandler(errorDialog);
		
		workingSetEditor.getButtonAddNewSite().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addSiteWorkflow.start();
			}
		});
		
		addSiteWorkflow.addSiteAddedHandler(new SiteAddedHandler() {
			
			@Override
			public void onSiteAdded(SiteAddedEvent event) {
				siteAddRemoveListModel.updateSites();
			}
		});
		
		//trigger update
		workingSetListModel.update(new NotificationCallback() {
			
			@Override
			public void finished() {
				if (workingSetListModel.getContentCount() > 0) {
					//TODO: remember last selected working set, cookie?
					workingSetListModel.setSelectedContent(0);
				}
			}
		});
		siteAddRemoveListModel.updateSites();
	}   
}
