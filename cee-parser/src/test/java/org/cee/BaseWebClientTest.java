package org.cee;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebResponse;

public class BaseWebClientTest {

    protected Reader createReader() {
        return mock(Reader.class);
    }

    protected WebClient createWebClient(Reader reader) throws IOException {
        WebClient webClient = mock(WebClient.class);
        WebResponse webResponse = mock(WebResponse.class);
        when(webResponse.openReader()).thenReturn(reader);
        when(webClient.openWebResponse(any(URL.class), any(Boolean.class))).thenReturn(webResponse);
        return webClient; 
    }

    protected void addReaderUrls(WebClient webClient, URL[] urls, Reader[] readers) throws IOException {
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            Reader reader = readers[i];
            WebResponse webResponse = mock(WebResponse.class);
            when(webResponse.openReader()).thenReturn(reader);
            when(webClient.openWebResponse(eq(url), any(Boolean.class))).thenReturn(webResponse);    
        } 
    }
    
    protected void addThrowsUrls(WebClient webClient, URL[] urls) throws IOException {
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            WebResponse webResponse = mock(WebResponse.class);
            when(webResponse.openReader()).thenThrow(new IOException());
            when(webClient.openWebResponse(eq(url), any(Boolean.class))).thenReturn(webResponse);    
        } 
    }
}
