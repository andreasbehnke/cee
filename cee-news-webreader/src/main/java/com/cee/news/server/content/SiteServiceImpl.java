package com.cee.news.server.content;

import java.util.List;

import com.cee.news.client.content.SiteService;
import com.cee.news.model.Site;
import com.cee.news.store.SiteStore;
import com.cee.news.store.StoreException;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;

public class SiteServiceImpl implements SiteService {
    
    private SiteStore siteStore;
    
    public void setSiteStore(SiteStore siteStore) {
        this.siteStore = siteStore;
    }

    public List<String> getSites() {
        try {
            return siteStore.getSitesOrderedByLocation();
        } catch (StoreException e) {
            throw new RuntimeException(e);
        }
    }
    
    public SafeHtml getTitle(String siteUrl) {
        try {
            Site site = siteStore.getSite(siteUrl);
            SafeHtmlBuilder builder = new SafeHtmlBuilder();
            String title = site.getTitle();
            builder.appendEscaped(title == null ? siteUrl : title);
            return builder.toSafeHtml();
        } catch (StoreException e) {
            throw new RuntimeException(e);
        }
    }

    public SafeHtml getHtmlDescription(String siteUrl) {
        try {
            Site site = siteStore.getSite(siteUrl);
            SafeHtmlBuilder builder = new SafeHtmlBuilder();
            String title = site.getTitle();
            String description = site.getDescription();
            builder.appendHtmlConstant("<h3>");
            builder.appendEscaped(title == null ? site.getLocation() : title);
            builder.appendHtmlConstant("</h3>");
            if (description != null) {
                builder.appendHtmlConstant("<div class=\"description\">");
                builder.append(SimpleHtmlSanitizer.sanitizeHtml(site.getDescription()));
                builder.appendHtmlConstant("</div>");
            }
            return builder.toSafeHtml();
        } catch (StoreException e) {
            throw new RuntimeException(e);
        }
    }
}
