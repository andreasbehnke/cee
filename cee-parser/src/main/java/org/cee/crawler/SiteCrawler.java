package org.cee.crawler;

import java.net.URL;

public interface SiteCrawler {

    void crawl(URL location, SiteCrawlerCallback siteCrawlerCallback, FollowConstraint... followConstraints) throws Exception;

}
