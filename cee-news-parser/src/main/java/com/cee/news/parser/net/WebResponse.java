package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public interface WebResponse {
    
    InputStream getStream() throws IOException;
    
    Reader getReader() throws IOException;

    String getContentType() throws IOException;
    
    String getContentEncoding() throws IOException;
    
    long getContentLength() throws IOException;
    
    URL getLocation() throws IOException;
}
