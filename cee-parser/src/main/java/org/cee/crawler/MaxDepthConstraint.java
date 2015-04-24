package org.cee.crawler;

import java.net.URL;

public class MaxDepthConstraint implements FollowConstraint {
    
    private int maxDepth;
    
    public MaxDepthConstraint(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean follow(int depth, URL link) {
        return depth < maxDepth;
    }

}
