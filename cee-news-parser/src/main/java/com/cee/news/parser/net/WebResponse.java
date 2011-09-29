package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface WebResponse {
    
    InputStream openStream() throws IOException;
    
    Reader openReader() throws IOException;

    String getContentType();
    
    long getContentLength();
}
