package org.cee.crawler;

import java.net.URL;

import org.cee.net.WebClient;

public interface SiteCrawler {

    void crawl(final WebClient webClient, URL location, SiteCrawlerCallback siteCrawlerCallback, FollowConstraint... followConstraints) throws Exception;

}
