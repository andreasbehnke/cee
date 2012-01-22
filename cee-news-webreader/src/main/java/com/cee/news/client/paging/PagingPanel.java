package com.cee.news.client.paging;

import java.util.List;

import com.cee.news.client.list.KeyLink;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;

public class PagingPanel extends Composite implements PagingView {
    
    private final Anchor anchorNext;
    private final ScrollPanel scrollPanelMainContent;
    private final Anchor anchorPrevious;
    private final HTML mainContent;
    private final ListBox comboBoxJumpTo;
    
    public PagingPanel() {
        
        FocusPanel focusPanel = new FocusPanel();
        initWidget(focusPanel);
        
        LayoutPanel layoutPanel = new LayoutPanel();
        focusPanel.setWidget(layoutPanel);
        layoutPanel.setSize("100%", "100%");
        
        anchorPrevious = new Anchor();
        anchorPrevious.setHref("javascript:void(0)");
        layoutPanel.add(anchorPrevious);
        layoutPanel.setWidgetLeftRight(anchorPrevious, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(anchorPrevious, 24.0, Unit.PX, 24.0, Unit.PX);
        
        scrollPanelMainContent = new ScrollPanel();
        layoutPanel.add(scrollPanelMainContent);
        layoutPanel.setWidgetLeftRight(scrollPanelMainContent, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopBottom(scrollPanelMainContent, 48.0, Unit.PX, 24.0, Unit.PX);
        
        mainContent = new HTML("", true);
        scrollPanelMainContent.setWidget(mainContent);
        mainContent.setSize("100%", "100%");
        
        anchorNext = new Anchor();
        anchorNext.setHref("javascript:void(0)");
        layoutPanel.add(anchorNext);
        layoutPanel.setWidgetLeftRight(anchorNext, 0.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetBottomHeight(anchorNext, 0.0, Unit.PX, 24.0, Unit.PX);
        
        comboBoxJumpTo = new ListBox();
        layoutPanel.add(comboBoxJumpTo);
        comboBoxJumpTo.setWidth("100%");
        layoutPanel.setWidgetLeftRight(comboBoxJumpTo, 111.0, Unit.PX, 0.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(comboBoxJumpTo, 0.0, Unit.PX, 24.0, Unit.PX);
        
        Label lblJumpToArticle = new Label("Jump to article:");
        layoutPanel.add(lblJumpToArticle);
        layoutPanel.setWidgetLeftWidth(lblJumpToArticle, 0.0, Unit.PX, 108.0, Unit.PX);
        layoutPanel.setWidgetTopHeight(lblJumpToArticle, 2.0, Unit.PX, 24.0, Unit.PX);
        
    }

    public void setPreviousEnabled(boolean enabled) {
        anchorPrevious.setVisible(enabled);
    }

    public HasSafeHtml getPreviousContent() {
        return anchorPrevious;
    }
    
    public void addPreviousClickHandler(ClickHandler handler) {
        anchorPrevious.addClickHandler(handler);
    }
    
    public void setNextEnabled(boolean enabled) {
        anchorNext.setVisible(enabled);
    }

    public HasSafeHtml getNextContent() {
        return anchorNext;
    }
    
    public void addNextClickHandler(ClickHandler handler) {
        anchorNext.addClickHandler(handler);
    }

    public HasSafeHtml getMainContent() {
        return mainContent;
    }
    
    @Override
    public void resetMainContentScrollPosition() {
    	scrollPanelMainContent.scrollToTop();
    }

    public void setJumpToLinks(List<KeyLink> links) {
        comboBoxJumpTo.clear();
        for (KeyLink link : links) {
            comboBoxJumpTo.addItem(link.getText(), link.getKeyValue());
        }
    }
    
    public void setJumpToSelectedIndex(int index) {
        comboBoxJumpTo.setSelectedIndex(index);
    }
    
    public int getJumpToSelectedIndex() {
        return comboBoxJumpTo.getSelectedIndex();
    }

    public void addJumpToChangedHandler(ChangeHandler handler) {
        comboBoxJumpTo.addChangeHandler(handler);
    }
}
