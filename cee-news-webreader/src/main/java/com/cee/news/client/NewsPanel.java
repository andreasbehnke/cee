package com.cee.news.client;

import com.cee.news.client.list.ListPanel;
import com.cee.news.client.list.ListView;
import com.cee.news.client.paging.PagingPanel;
import com.cee.news.client.paging.PagingView;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Label;

public class NewsPanel extends Composite implements NewsView {

    private Button buttonGoToStart;
	private PagingPanel pagingView;
	private ListPanel whatOthersSayListView;
	private Label siteNameLabel;

	public NewsPanel() {
        
        LayoutPanel layoutPanel = new LayoutPanel();
        initWidget(layoutPanel);
        layoutPanel.setSize("963px", "470px");
        
        buttonGoToStart = new Button("Go to Start");
        layoutPanel.add(buttonGoToStart);
        layoutPanel.setWidgetLeftWidth(buttonGoToStart, 0.0, Unit.PX, 124.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(buttonGoToStart, 0.0, Unit.PX, 24.0, Unit.PX);
        
        pagingView = new PagingPanel();
        layoutPanel.add(pagingView);
        layoutPanel.setWidgetLeftRight(pagingView, 0.0, Unit.PX, 333.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(pagingView, 52.0, Unit.PX, 0.0, Unit.PX);
        
        whatOthersSayListView = new ListPanel();
        layoutPanel.add(whatOthersSayListView);
        layoutPanel.setWidgetRightWidth(whatOthersSayListView, 0.0, Unit.PX, 327.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(whatOthersSayListView, 52.0, Unit.PX, 0.0, Unit.PX);
        
        InlineLabel nlnlblCurrentSite = new InlineLabel("Current Site:");
        layoutPanel.add(nlnlblCurrentSite);
        layoutPanel.setWidgetLeftWidth(nlnlblCurrentSite, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblCurrentSite, 30.0, Unit.PX, 16.0, Unit.PX);
        
        InlineLabel nlnlblNewInlinelabel = new InlineLabel("What others say:");
        layoutPanel.add(nlnlblNewInlinelabel);
        layoutPanel.setWidgetRightWidth(nlnlblNewInlinelabel, 178.0, Unit.PX, 149.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblNewInlinelabel, 30.0, Unit.PX, 16.0, Unit.PX);
        
        siteNameLabel = new Label("");
        layoutPanel.add(siteNameLabel);
        layoutPanel.setWidgetLeftWidth(siteNameLabel, 111.0, Unit.PX, 56.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(siteNameLabel, 30.0, Unit.PX, 16.0, Unit.PX);
    }

	/* (non-Javadoc)
	 * @see com.cee.news.client.NewsView#getButtonGoToStart()
	 */
	@Override
	public HasClickHandlers getButtonGoToStart() {
		return buttonGoToStart;
	}

	/* (non-Javadoc)
	 * @see com.cee.news.client.NewsView#getPagingView()
	 */
	@Override
	public PagingView getPagingView() {
		return pagingView;
	}

	/* (non-Javadoc)
	 * @see com.cee.news.client.NewsView#getWhatOthersSayListView()
	 */
	@Override
	public ListView getWhatOthersSayListView() {
		return whatOthersSayListView;
	}
	
	@Override
	public HasText getSiteNameLabel() {
		return siteNameLabel;
	}
}
