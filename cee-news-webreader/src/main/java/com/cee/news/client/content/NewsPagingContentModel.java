package com.cee.news.client.content;

import java.util.ArrayList;
import java.util.List;

import com.cee.news.client.list.DefaultListModel;
import com.cee.news.client.list.LinkValue;
import com.cee.news.client.paging.PagingContentModel;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * {@link PagingContentModel} which calls remote news service asynchronously
 */
public class NewsPagingContentModel extends DefaultListModel implements PagingContentModel {
    
    private final NewsServiceAsync service = NewsService.Util.getInstance();
    
    private String siteLocation;
    
    private List<String> headlines;
    
    public void setSelectedSite(String siteLocation) {
        this.siteLocation = siteLocation;
        service.getHeadlines(siteLocation, new AsyncCallback<List<String>>() {
            public void onSuccess(List<String> result) {
                setHeadlines(result);
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load headlines!");//TODO: i18n
            }
        });
    }
    
    protected void setHeadlines(List<String> headlines) {
        this.headlines = headlines;
        List<LinkValue> links = new ArrayList<LinkValue>();
        int count = 0;
        for (String headline : headlines) {
            links.add(new LinkValue(count, headline));
            count++;
        }
        fireContentListChanged(links);
        setSelectedContent(0);
    }

    public int getContentCount() {
        if (headlines == null) {
            return 0;
        }
        return headlines.size();
    }

    public void getContentTitle(final HasSafeHtml target, int index) {
        if (headlines == null) {
            throw new IllegalStateException("Headlines have not been set yet!");
        }
        target.setHTML(SimpleHtmlSanitizer.sanitizeHtml(headlines.get(index)));
    }

    public void getContentDescription(final HasSafeHtml target, int index) {
        service.getHtmlDescription(siteLocation, index, new AsyncCallback<SafeHtml>() {
            public void onSuccess(SafeHtml result) {
                target.setHTML(result);
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load description!");//TODO: i18n
            }
        });
    }

    public void getContent(final HasSafeHtml target, int index) {
        service.getHtmlContent(siteLocation, index, new AsyncCallback<SafeHtml>() {
            public void onSuccess(SafeHtml result) {
                target.setHTML(result);
            }
            public void onFailure(Throwable caught) {
                fireErrorEvent(caught, "Could not load content!");//TODO: i18n
            }
        });
    }
}
