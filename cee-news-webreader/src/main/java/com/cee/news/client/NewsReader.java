package com.cee.news.client;

import com.cee.news.client.content.NewSiteWizard;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.content.SiteUpdateService;
import com.cee.news.client.content.SiteUpdateServiceAsync;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.list.ListPresenter;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.workingset.WorkingSetEditor;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.WorkingSetWorkflow;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	
	/**
	 * Application event bus. The following events are routed:
	 * 
	 * {@link ErrorEvent} Event raised for all unknown exceptions
	 * {@link SelectionChangedEvent} Event raised if the current working set is changed
	 */
	private final SimpleEventBus appEventBus = new SimpleEventBus();
	
	private final ErrorHandler globalErrorHandler = new ErrorHandler() {
		@Override
		public void onError(ErrorEvent event) {
			appEventBus.fireEvent(event);
		}
	};
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		ErrorDialog errorDialog = new ErrorDialog();
		appEventBus.addHandler(ErrorEvent.TYPE, errorDialog);
		
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
		
		//Working Set Selection
		final WorkingSetListModel workingSetListModel = new WorkingSetListModel();
		workingSetListModel.addErrorHandler(globalErrorHandler);
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChange(SelectionChangedEvent event) {
				appEventBus.fireEvent(event);
			}
		});
		final WorkingSetSelectionView workingSetSelectionView = startPanel.getWorkingSetSelectionView();
		
		//Site Update Service
		final SiteUpdateServiceAsync siteUpdateService = SiteUpdateService.Util.getInstance();
		appEventBus.addHandler(SelectionChangedEvent.TYPE, new SelectionChangedHandler() {
			@Override
			public void onSelectionChange(SelectionChangedEvent event) {
				siteUpdateService.addSitesOfWorkingSetToUpdateQueue(event.getKey(), new AsyncCallback<Integer>() {
					@Override
					public void onSuccess(Integer result) {
						// TODO Start progress bar
					}
					@Override
					public void onFailure(Throwable caught) {
						appEventBus.fireEvent(new ErrorEvent(caught, "Could not queue commands for working set update"));
					}
				});
			}
		});
		
		//Latest Article List
		final NewsListContentModel newsListContentModel = new NewsListContentModel();
		new ListPresenter(newsListContentModel, newsListContentModel, startPanel.getLatestArticlesListView());
		appEventBus.addHandler(SelectionChangedEvent.TYPE, new SelectionChangedHandler() {
			@Override
			public void onSelectionChange(SelectionChangedEvent event) {
				newsListContentModel.updateFromWorkingSet(event.getKey());
			}
		});
		
		//Site List
		final SiteListContentModel sitesOfWorkingSetModel = new SiteListContentModel();
		sitesOfWorkingSetModel.addErrorHandler(globalErrorHandler);
		new ListPresenter(sitesOfWorkingSetModel, sitesOfWorkingSetModel, startPanel.getSitesListView());
		appEventBus.addHandler(SelectionChangedEvent.TYPE, new SelectionChangedHandler() {
			@Override
			public void onSelectionChange(SelectionChangedEvent event) {
				sitesOfWorkingSetModel.update(null, event.getKey());
			}
		});

		//New & Edit Working Set Workflow
		final NewSiteWizardView newSiteWizard = new NewSiteWizard();
		final SiteListContentModel siteAddRemoveListModel = new SiteListContentModel();
		siteAddRemoveListModel.addErrorHandler(globalErrorHandler);
		final WorkingSetEditor workingSetEditor = new WorkingSetEditor(siteAddRemoveListModel, siteAddRemoveListModel);
		final WorkingSetWorkflow workingSetWorkflow = new WorkingSetWorkflow(workingSetListModel, siteAddRemoveListModel, workingSetEditor, newSiteWizard);
		workingSetWorkflow.addErrorHandler(globalErrorHandler);
		new WorkingSetSelectionPresenter(workingSetListModel, workingSetSelectionView);
		workingSetSelectionView.getNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				workingSetWorkflow.newWorkingSet();
			}
		});
		workingSetSelectionView.getEditButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				workingSetWorkflow.editWorkingSet(workingSetListModel.getSelectedKey());
			}
		});
		
		//trigger update
		workingSetListModel.update(null);
		siteAddRemoveListModel.update(null);
	}
}