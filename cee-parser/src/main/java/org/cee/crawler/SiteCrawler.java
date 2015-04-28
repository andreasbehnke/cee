package org.cee.crawler;

import java.net.URL;

public interface SiteCrawler {

	SiteCrawler setPageHandler(PageHandler pageHandler);
    
    SiteCrawler followIf(FollowConstraint followConstraint);
    
    void crawl(URL location) throws InterruptedException;
}
