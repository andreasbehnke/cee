package com.cee.news.client;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.AddSiteToWorkingSet;
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
import com.cee.news.client.error.ErrorEvent;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.list.SelectionListChangedEvent;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.cee.news.client.paging.PagingPresenter;
import com.cee.news.client.progress.ProgressModel;
import com.cee.news.client.progress.ProgressPresenter;
import com.cee.news.client.search.SearchPresenter;
import com.cee.news.client.workingset.WorkingSetEditor;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.WorkingSetWorkflow;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class NewsReader implements EntryPoint {
	
	public void onModuleLoad() {
	    final ClientFactory clientFactory = GWT.create(ClientFactory.class);
		final ErrorHandler errorHandler = clientFactory.getGlobalErrorHandler();
	    
		//Working Set Selection
		final WorkingSetListModel workingSetListModel = new WorkingSetListModel();
		workingSetListModel.addErrorHandler(errorHandler);
		final WorkingSetSelectionView workingSetSelectionView = clientFactory.getWorkingSetSelectionView();
		
		//Site Update Service
		final SiteUpdateServiceAsync siteUpdateService = SiteUpdateServiceAsync.Util.getInstance();
		final ProgressModel progressModel = new ProgressModel();
		new ProgressPresenter(progressModel, clientFactory.getProgressView());
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
		final CellListPresenter newsListPresenter = new SingleSelectionCellListPresenter(clientFactory.getCellListLatestArticles(), filteredContentList, filteredContentList);
		newsListPresenter.addErrorHandler(errorHandler);
		
		//Site List
		final SiteListContentModel sitesOfWorkingSetModel = new SiteListContentModel();
		sitesOfWorkingSetModel.addErrorHandler(errorHandler);
		final SourceSelectionView sourceSelectionView = clientFactory.getSourceSelectionView();
		new SourceSelectionPresenter(sourceSelectionView, sitesOfWorkingSetModel, errorHandler);
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
            @Override
            public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
                sitesOfWorkingSetModel.findSitesOfWorkingSet(event.getKey().getKey());
            }
        });
		
		//Search
		new SearchPresenter(filteredContentList, sitesOfWorkingSetModel, clientFactory.getSearchView());
		
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
		
		//Add Source to Working Set Workflow
		final NewSiteWizardView addSiteWizard = new NewSiteWizard();
        final AddSiteToWorkingSet addSiteToWorkingSet = new AddSiteToWorkingSet(addSiteWizard, workingSetListModel);
		addSiteToWorkingSet.addErrorHandler(errorHandler);
		sourceSelectionView.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addSiteToWorkingSet.start();
            }
        });
        
		//News Paging View
		new PagingPresenter<EntityKey>(filteredContentList, filteredContentList, new EntityKeyLinkResolver(), clientFactory.getNewsPagingView());
		sitesOfWorkingSetModel.addSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
            @Override
            public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
                filteredContentList.getNewsOfSites(EntityKeyUtil.extractKeys(event.getKeys()));
            }
        });
		clientFactory.getButtonGoToStart().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    clientFactory.getPageSwitchView().showStartPage();
			}
		});
		
		//What others say view
		final NewsListContentModel whatOthersSay = new NewsListContentModel();
		new SingleSelectionCellListPresenter(clientFactory.getWhatOthersSayCellList(), whatOthersSay, whatOthersSay);
		filteredContentList.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
			
			@Override
			public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
				whatOthersSay.getRelatedArticles(event.getKey().getKey(), workingSetListModel.getSelectedKey().getKey(), new NotificationCallback() {
                    @Override
                    public void finished() {
                        clientFactory.getPageSwitchView().showNewsPage();
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
					clientFactory.getSiteNameLabel().setText(siteKey);
					
					//TODO should we update the site?
					
					filteredContentList.getNewsOfSite(siteKey, new NotificationCallback() {
						
						@Override
						public void finished() {
						    filteredContentList.setSelectedKey(event.getKey());
						}
					});
				}
			}
		});
		
		//trigger update
		siteAddRemoveListModel.findAllSites();
		workingSetListModel.findAllWorkingSets(new NotificationCallback() {
            
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