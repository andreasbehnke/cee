package com.cee.news.client;

import com.cee.news.client.content.EntityKeyCell;
import com.cee.news.client.content.EntityKeyProvider;
import com.cee.news.client.list.ScrollLoader;
import com.cee.news.client.paging.PagingPanel;
import com.cee.news.client.paging.PagingView;
import com.cee.news.model.EntityKey;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;

public class NewsPanel extends Composite implements NewsView {

    private Button buttonGoToStart;
	private PagingPanel pagingView;
	private CellList<EntityKey> whatOthersSayCellList;
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
        
        whatOthersSayCellList = new CellList<EntityKey>(new EntityKeyCell(), new EntityKeyProvider());
        ScrollLoader whatOthersSayScroller = new ScrollLoader();
        whatOthersSayScroller.setDisplay(whatOthersSayCellList);
        layoutPanel.add(whatOthersSayScroller);
        layoutPanel.setWidgetRightWidth(whatOthersSayScroller, 0.0, Unit.PX, 327.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(whatOthersSayScroller, 52.0, Unit.PX, 0.0, Unit.PX);
        
        InlineLabel nlnlblCurrentSite = new InlineLabel("Current Site:");
        layoutPanel.add(nlnlblCurrentSite);
        layoutPanel.setWidgetLeftWidth(nlnlblCurrentSite, 0.0, Unit.PX, 90.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblCurrentSite, 30.0, Unit.PX, 16.0, Unit.PX);
        
        InlineLabel nlnlblNewInlinelabel = new InlineLabel("What others say:");
        layoutPanel.add(nlnlblNewInlinelabel);
        layoutPanel.setWidgetRightWidth(nlnlblNewInlinelabel, 178.0, Unit.PX, 149.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(nlnlblNewInlinelabel, 30.0, Unit.PX, 16.0, Unit.PX);
        
        siteNameLabel = new InlineLabel();
        layoutPanel.add(siteNameLabel);
        layoutPanel.setWidgetLeftRight(siteNameLabel, 84.0, Unit.PX, 333.0, Unit.PX);
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
	public PagingView getNewsPagingView() {
		return pagingView;
	}

	@Override
	public CellList<EntityKey> getWhatOthersSayCellList() {
		return whatOthersSayCellList;
	}
	
	@Override
	public HasText getSiteNameLabel() {
		return siteNameLabel;
	}
}
