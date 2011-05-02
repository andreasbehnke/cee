package com.cee.news.net;

import java.io.IOException;
import java.io.InputStream;

public interface WebResponse {
    
    InputStream openStream() throws IOException;

    String getContentType();
    
    long getContentLength();
}
