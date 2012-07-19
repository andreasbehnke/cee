package com.cee.news.client;

import com.cee.news.client.async.NotificationCallback;
import com.cee.news.client.content.AddSiteToWorkingSet;
import com.cee.news.client.content.ArticleKeyLinkProvider;
import com.cee.news.client.content.NewsListContentModel;
import com.cee.news.client.content.RelatedArticlesContentModel;
import com.cee.news.client.content.SingleSelectionCellListPresenter;
import com.cee.news.client.content.SiteListContentModel;
import com.cee.news.client.content.SourceSelectionPresenter;
import com.cee.news.client.content.SourceSelectionView;
import com.cee.news.client.error.ErrorHandler;
import com.cee.news.client.list.CellListPresenter;
import com.cee.news.client.list.SelectionChangedEvent;
import com.cee.news.client.list.SelectionChangedHandler;
import com.cee.news.client.list.SelectionListChangedEvent;
import com.cee.news.client.list.SelectionListChangedHandler;
import com.cee.news.client.paging.PagingPresenter;
import com.cee.news.client.search.SearchPresenter;
import com.cee.news.client.workingset.WorkingSetListModel;
import com.cee.news.client.workingset.WorkingSetSelectionPresenter;
import com.cee.news.client.workingset.WorkingSetSelectionView;
import com.cee.news.client.workingset.WorkingSetView;
import com.cee.news.client.workingset.WorkingSetWorkflow;
import com.cee.news.model.ArticleKey;
import com.cee.news.model.EntityKey;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class NewsReader implements EntryPoint {
	
	public void onModuleLoad() {
	    final ClientFactory clientFactory = GWT.create(ClientFactory.class);
		final ErrorHandler errorHandler = clientFactory.getGlobalErrorHandler();
		final StartView startView = clientFactory.getStartView();
        
		//Working Set Selection
		final WorkingSetListModel workingSetListModel = new WorkingSetListModel();
		workingSetListModel.addErrorHandler(errorHandler);
		final WorkingSetSelectionView workingSetSelectionView = startView.getWorkingSetSelectionView();
		
		//Filtered content list
		final NewsListContentModel filteredContentList = new NewsListContentModel();
		filteredContentList.addErrorHandler(errorHandler);
		final CellListPresenter<ArticleKey> newsListPresenter = new SingleSelectionCellListPresenter<ArticleKey>(
		        startView.getCellListLatestArticles(), 
		        filteredContentList, 
		        filteredContentList,
		        startView.getNumberOfVisibleArticleTeasers());
		newsListPresenter.addErrorHandler(errorHandler);
		startView.getButtonRefresh().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                filteredContentList.refresh();
            }
        });
		
		//Site List
		final SiteListContentModel sitesOfWorkingSetModel = new SiteListContentModel();
		sitesOfWorkingSetModel.addErrorHandler(errorHandler);
		final SourceSelectionView sourceSelectionView = startView.getSourceSelectionView();
		new SourceSelectionPresenter(sourceSelectionView, sitesOfWorkingSetModel, errorHandler);
		workingSetListModel.addSelectionChangedhandler(new SelectionChangedHandler<EntityKey>() {
            @Override
            public void onSelectionChange(SelectionChangedEvent<EntityKey> event) {
                sitesOfWorkingSetModel.findSitesOfWorkingSet(event.getKey(), new NotificationCallback() {      
                    @Override
                    public void finished() {
                        sitesOfWorkingSetModel.setSelections(sitesOfWorkingSetModel.getKeys());
                    }
                });
            }
        });
		
		//Search
		new SearchPresenter(filteredContentList, startView.getSearchView());
		
		//New & Edit Working Set Workflow
		final SiteListContentModel siteAddRemoveListModel = new SiteListContentModel();
		siteAddRemoveListModel.addErrorHandler(errorHandler);
		final WorkingSetView workingSetView = clientFactory.getWorkingSetView();
		final WorkingSetWorkflow workingSetWorkflow = new WorkingSetWorkflow(workingSetListModel, siteAddRemoveListModel, workingSetView, clientFactory.getNewSiteWizardView());
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
				workingSetWorkflow.editWorkingSet(workingSetListModel.getSelectedKey());
			}
		});
		
		//Add Source to Working Set Workflow
		final AddSiteToWorkingSet addSiteToWorkingSet = new AddSiteToWorkingSet(clientFactory.getAddSiteWizardView(), workingSetListModel);
		addSiteToWorkingSet.addErrorHandler(errorHandler);
		sourceSelectionView.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addSiteToWorkingSet.start();
            }
        });
        
		//News Paging View
		final NewsView newsView = clientFactory.getNewsView();
		new PagingPresenter<ArticleKey>(filteredContentList, filteredContentList, new ArticleKeyLinkProvider(), newsView.getNewsPagingView());
		sitesOfWorkingSetModel.addSelectionListChangedHandler(new SelectionListChangedHandler<EntityKey>() {
            @Override
            public void onSelectionListChanged(SelectionListChangedEvent<EntityKey> event) {
                filteredContentList.getNewsOfSites(event.getKeys());
            }
        });
		newsView.getButtonGoToStart().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			    clientFactory.getPageSwitchView().showStartPage();
			}
		});
		
		//What others say view
		final RelatedArticlesContentModel whatOthersSay = new RelatedArticlesContentModel();
		new SingleSelectionCellListPresenter<ArticleKey>(
		        newsView.getWhatOthersSayCellList(), 
		        whatOthersSay, 
		        whatOthersSay, 
		        newsView.getNumberOfVisibleRelatedArticles());
		filteredContentList.addSelectionChangedhandler(new SelectionChangedHandler<ArticleKey>() {
			@Override
			public void onSelectionChange(SelectionChangedEvent<ArticleKey> event) {
				whatOthersSay.getRelatedArticles(event.getKey(), workingSetListModel.getSelectedKey(), new NotificationCallback() {
                    @Override
                    public void finished() {
                        clientFactory.getPageSwitchView().showNewsPage();
                    }
                });
			}
		});
		whatOthersSay.addSelectionChangedhandler(new SelectionChangedHandler<ArticleKey>() {
			@Override
			public void onSelectionChange(final SelectionChangedEvent<ArticleKey> event) {
				if (event.isUserAction()) {
					final ArticleKey articleKey = event.getKey();
					final String siteKey = articleKey.getSiteKey();
					newsView.getSiteNameLabel().setText(siteKey);
					filteredContentList.getNewsOfSite(EntityKey.get(siteKey), new NotificationCallback() {
						@Override
						public void finished() {
						    filteredContentList.setSelectedKey(event.getKey());
						}
					});
				}
			}
		});
		newsView.getButtonRefresh().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                filteredContentList.refresh(new NotificationCallback() {
                    @Override
                    public void finished() {
                        whatOthersSay.refresh();
                    }
                });
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