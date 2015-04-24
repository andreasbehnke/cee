package org.cee.crawler;

import java.net.URL;

import org.cee.net.WebClient;

public class SupportedProtocolConstraint implements FollowConstraint {

    private final WebClient webClient;
    
    public SupportedProtocolConstraint(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public boolean follow(int depth, URL link) {
        return webClient.isSupported(link);
    }

}
