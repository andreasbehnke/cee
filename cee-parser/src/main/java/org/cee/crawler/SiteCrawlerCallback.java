package org.cee.crawler;

import java.net.URL;

import org.xml.sax.ContentHandler;

public interface SiteCrawlerCallback {

    public ContentHandler[] onPageStart(URL location);
    
    public void onPageFinished(URL location, ContentHandler[] contentHandlers);
}
