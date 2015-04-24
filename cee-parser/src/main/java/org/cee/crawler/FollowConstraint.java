package org.cee.crawler;

import java.net.URL;

public interface FollowConstraint {

    public boolean follow(int depth, URL link);
}
