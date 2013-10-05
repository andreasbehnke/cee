package org.cee.webreader.client;

import org.cee.news.model.ArticleKey;
import org.cee.news.model.EntityKey;
import org.cee.webreader.client.async.NotificationCallback;
import org.cee.webreader.client.content.AddSiteToWorkingSet;
import org.cee.webreader.client.content.ArticleKeyLinkProvider;
import org.cee.webreader.client.content.LanguageListModel;
import org.cee.webreader.client.content.NewSiteWizardView;
import org.cee.webreader.client.content.NewsListContentModel;
import org.cee.webreader.client.content.RelatedArticlesContentModel;
import org.cee.webreader.client.content.SingleSelectionCellListPresenter;
import org.cee.webreader.client.content.SiteAddedEvent;
import org.cee.webreader.client.content.SiteAddedHandler;
import org.cee.webreader.client.content.SiteListContentModel;
import org.cee.webreader.client.content.SourceSelectionPresenter;
import org.cee.webreader.client.content.SourceSelectionView;
import org.cee.webreader.client.error.ErrorHandler;
import org.cee.webreader.client.list.CellListPresenter;
import org.cee.webreader.client.list.ListChangedEvent;
import org.cee.webreader.client.list.ListChangedHandler;
import org.cee.webreader.client.list.SelectionChangedEvent;
import org.cee.webreader.client.list.SelectionChangedHandler;
import org.cee.webreader.client.list.SelectionListChangedEvent;
import org.cee.webreader.client.list.SelectionListChangedHandler;
import org.cee.webreader.client.paging.PagingPresenter;
import org.cee.webreader.client.search.SearchPresenter;
import org.cee.webreader.client.workingset.WorkingSetListModel;
import org.cee.webreader.client.workingset.WorkingSetSelectionPresenter;
import org.cee.webreader.client.workingset.WorkingSetSelectionView;
import org.cee.webreader.client.workingset.WorkingSetView;
import org.cee.webreader.client.workingset.WorkingSetWorkflow;

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
		workingSetListModel.addSelectionChangedhandler(filteredContentList);
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
            	EntityKey workingSetKey = event.getKey();
            	if (workingSetKey == null) {
            		sitesOfWorkingSetModel.clear();
            	} else {
	                sitesOfWorkingSetModel.findSitesOfWorkingSet(workingSetKey, new NotificationCallback() {      
	                    @Override
	                    public void finished() {
	                        sitesOfWorkingSetModel.setSelections(sitesOfWorkingSetModel.getKeys());
	                    }
	                });
            	}
            }
        });
		
		//Search
		new SearchPresenter(filteredContentList, startView.getSearchView());
		
		//New & Edit Working Set Workflow
		final LanguageListModel languageListModel = new LanguageListModel();
		final SiteListContentModel siteAddRemoveListModel = new SiteListContentModel();
		siteAddRemoveListModel.addErrorHandler(errorHandler);
		final WorkingSetView workingSetView = clientFactory.getWorkingSetView();
		final WorkingSetWorkflow workingSetWorkflow = new WorkingSetWorkflow(workingSetListModel, siteAddRemoveListModel, languageListModel, workingSetView, clientFactory.getNewSiteWizardView(), clientFactory.createConfirmView());
		workingSetWorkflow.addErrorHandler(errorHandler);
		new WorkingSetSelectionPresenter(workingSetListModel, workingSetSelectionView, workingSetWorkflow);
		
		//Add new Source to Working Set Workflow
		final NewSiteWizardView addSiteWizardView = clientFactory.getAddSiteWizardView();
		final AddSiteToWorkingSet addSiteToWorkingSet = new AddSiteToWorkingSet(addSiteWizardView, workingSetListModel, clientFactory.createNotificationView());
		addSiteToWorkingSet.addErrorHandler(errorHandler);
		sourceSelectionView.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                addSiteToWorkingSet.start();
            }
        });
		addSiteToWorkingSet.addSiteAddedHandler(new SiteAddedHandler() {
			@Override
			public void onSiteAdded(final SiteAddedEvent event) {
				siteAddRemoveListModel.findAllSites(new NotificationCallback() {
					
					@Override
					public void finished() {
						siteAddRemoveListModel.addSelection(event.getEntityKey());
					}
				});
			}
		});
		languageListModel.addListChangedHandler(new ListChangedHandler<EntityKey>() {
			
			@Override
			public void onContentListChanged(ListChangedEvent<EntityKey> event) {
				addSiteWizardView.setAvailableLanguages(event.getValues());
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
					if (filteredContentList.getKeys().contains(articleKey)) {
						filteredContentList.setSelectedKey(articleKey);
					} else {
						filteredContentList.getContent(newsView.getNewsPagingView().getMainContent(), articleKey);
					}
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
		languageListModel.findAllLanguages();
		siteAddRemoveListModel.findAllSites();
		workingSetListModel.findAllWorkingSets(new NotificationCallback() {
            @Override
            public void finished() {
                if (workingSetListModel.getContentCount() > 0) {
                    EntityKey firstWorkingSet = workingSetListModel.getKeys().get(0);
                    workingSetListModel.setSelectedKey(firstWorkingSet);
                } else {
                	workingSetListModel.setSelectedKey(null);
                }
            }
        });
        
	}
}