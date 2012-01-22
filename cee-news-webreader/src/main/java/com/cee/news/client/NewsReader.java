package com.cee.news.client;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.ArticleUtil;
import com.cee.news.client.content.CellListPresenter;
import com.cee.news.client.content.EntityKeyLinkResolver;
import com.cee.news.client.content.EntityKeyUtil;
import com.cee.news.client.content.NewSiteWizard;
import com.cee.news.client.content.NewSiteWizardView;
import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.content.SingleSelectionCellListPresenter;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.content.SiteUpdateServiceAsync;
import com.cee.news.client.content.SourceSelectionPresenter;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.error.ErrorDialog;
import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.list.SelectionListChangedEvent;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.cee.news.client.paging.PagingPresenter;
import com.cee.news.client.progress.ProgressModel;
import com.cee.news.client.progress.ProgressPresenter;
import com.cee.news.client.workingset.WorkingSetEditor;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.WorkingSetWorkflow;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	
	private static final int NEWS_PANEL_INDEX = 1;

	private static final int START_PANEL_INDEX = 0;
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		final ErrorHandler errorHandler = new ErrorDialog();
		
		LayoutPanel layoutPanel = new LayoutPanel();
		rootPanel.add(layoutPanel, 0, 0);
		layoutPanel.setSize("100%", "100%");
		
		final DeckPanel deckPanel = new DeckPanel();
		layoutPanel.add(deckPanel);
		deckPanel.setSize("100%", "100%");
		layoutPanel.setWidgetLeftRight(deckPanel, 0.0, Unit.PX, 0.0, Unit.PX);
		layoutPanel.setWidgetTopBottom(deckPanel, 0.0, Unit.PX, 0.0, Unit.PX);
		
		StartPanel startPanel = new StartPanel();
		deckPanel.add(startPanel);
		startPanel.setSize("100%", "100%");
		
		final NewsPanel newsPanel = new NewsPanel();
		deckPanel.add(newsPanel);
		newsPanel.setSize("100%", "100%");
		deckPanel.showWidget(START_PANEL_INDEX);
		
		//Working Set Selection
		final WorkingSetListModel workingSetListModel = new WorkingSetListModel();
		workingSetListModel.addErrorHandler(errorHandler);
		final WorkingSetSelectionView workingSetSelectionView = startPanel.getWorkingSetSelectionView();
		
		//Site Update Service
		final SiteUpdateServiceAsync siteUpdateService = SiteUpdateServiceAsync.Util.getInstance();
		final ProgressModel progressModel = new ProgressModel();
		new ProgressPresenter(progressModel, startPanel.getProgressView());
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
			@Override
			public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
				siteUpdateService.addSitesOfWorkingSetToUpdateQueue(event.getKey().getKey(), new AsyncCallback<Integer>() {
					@Override
					public void onSuccess(Integer result) {
						progressModel.startMonitor();
					}
					@Override
					public void onFailure(Throwable caught) {
					    errorHandler.onError(new ErrorEvent(caught, "Could not queue commands for working set update"));
					}
				});
			}
		});
		
		//Filtered content list
		final NewsListContentModel filteredContentList = new NewsListContentModel();
		filteredContentList.addErrorHandler(errorHandler);
		final CellListPresenter newsListPresenter = new SingleSelectionCellListPresenter(startPanel.getCellListLatestArticles(), filteredContentList, filteredContentList);
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
			@Override
			public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
				filteredContentList.updateFromWorkingSet(event.getKey().getKey());
			}
		});
		newsListPresenter.addErrorHandler(errorHandler);
		final ProgressUpdateHandler progressUpdateHandler = new ProgressUpdateHandler(filteredContentList);
		workingSetListModel.addSelectionChangedhandler(progressUpdateHandler);
		progressModel.addProgressHandler(progressUpdateHandler);
		
		//Site List
		final SiteListContentModel sitesOfWorkingSetModel = new SiteListContentModel();
		sitesOfWorkingSetModel.addErrorHandler(errorHandler);
		final SourceSelectionView sourceSelectionView = startPanel.getSourceSelectionView();
		new SourceSelectionPresenter(sourceSelectionView, sitesOfWorkingSetModel, errorHandler);
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
            @Override
            public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
                sitesOfWorkingSetModel.update(event.getKey().getKey());
            }
        });
		
		//New & Edit Working Set Workflow
		final NewSiteWizardView newSiteWizard = new NewSiteWizard();
		final SiteListContentModel siteAddRemoveListModel = new SiteListContentModel();
		siteAddRemoveListModel.addErrorHandler(errorHandler);
		final WorkingSetEditor workingSetEditor = new WorkingSetEditor(siteAddRemoveListModel, siteAddRemoveListModel);
		final WorkingSetWorkflow workingSetWorkflow = new WorkingSetWorkflow(workingSetListModel, siteAddRemoveListModel, workingSetEditor, newSiteWizard);
		workingSetWorkflow.addErrorHandler(errorHandler);
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
				workingSetWorkflow.editWorkingSet(workingSetListModel.getSelectedKey().getKey());
			}
		});
		
		//News Paging View
		new PagingPresenter<EntityKey>(filteredContentList, filteredContentList, new EntityKeyLinkResolver(), newsPanel.getPagingView());
		sitesOfWorkingSetModel.addSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
            @Override
            public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
                filteredContentList.updateFromSites(EntityKeyUtil.extractKeys(event.getKeys()));
            }
        });
		newsPanel.getButtonGoToStart().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deckPanel.showWidget(START_PANEL_INDEX);
			}
		});
		
		//What others say view
		final NewsListContentModel whatOthersSay = new NewsListContentModel();
		new SingleSelectionCellListPresenter(newsPanel.getWhatOthersSayCellList(), whatOthersSay, whatOthersSay);
		filteredContentList.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
			
			@Override
			public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
				whatOthersSay.updateFromArticle(event.getKey().getKey(), workingSetListModel.getSelectedKey().getKey(), new NotificationCallback() {
                    @Override
                    public void finished() {
                        deckPanel.showWidget(NEWS_PANEL_INDEX);
                    }
                });
			}
		});
		whatOthersSay.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
			@Override
			public void onSelectionChange(final SelectionChangedEvent<EntityKey> event) {
				if (event.isUserAction()) {
					final String articleKey = event.getKey().getKey();
					final String siteKey = ArticleUtil.getSiteKeyFromArticleKey(articleKey);
					newsPanel.getSiteNameLabel().setText(siteKey);
					
					//TODO should we update the site?
					
					filteredContentList.updateFromSite(siteKey, new NotificationCallback() {
						
						@Override
						public void finished() {
						    filteredContentList.setSelectedKey(event.getKey());
						}
					});
				}
			}
		});
		
		//trigger update
		siteAddRemoveListModel.update();
		workingSetListModel.update(new NotificationCallback() {
            
            @Override
            public void finished() {
                if (workingSetListModel.getContentCount() > 0) {
                    EntityKey firstWorkingSet = workingSetListModel.getKeys().get(0);
                    workingSetListModel.setSelectedKey(firstWorkingSet);
                }
            }
        });
        
	}
}