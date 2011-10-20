package com.cee.news.parser.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface WebResponse {
    
    InputStream getStream() throws IOException;
    
    Reader getReader() throws IOException;

    String getContentType();
    
    String getContentEncoding();
    
    long getContentLength();
}
