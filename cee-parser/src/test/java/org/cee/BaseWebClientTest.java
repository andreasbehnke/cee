package org.cee;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.cee.parser.net.ReaderSource;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebResponse;

public class BaseWebClientTest {

    protected Reader createReader() {
        return mock(Reader.class);
    }

    protected WebClient createWebClient(Reader reader) throws IOException {
        WebClient webClient = mock(WebClient.class);
        WebResponse webResponse = mock(WebResponse.class);
        ReaderSource readerSource = mock(ReaderSource.class);
        when(readerSource.getReader()).thenReturn(reader);
        when(webResponse.openReaderSource()).thenReturn(readerSource);
        when(webClient.openWebResponse(any(URL.class))).thenReturn(webResponse);
        return webClient; 
    }

    protected void addReaderUrls(WebClient webClient, URL[] urls, Reader[] readers) throws IOException {
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            Reader reader = readers[i];
            ReaderSource readerSource = mock(ReaderSource.class);
            WebResponse webResponse = mock(WebResponse.class);
            when(readerSource.getReader()).thenReturn(reader);
            when(webResponse.openReaderSource()).thenReturn(readerSource);
            when(webClient.openWebResponse(eq(url))).thenReturn(webResponse);    
        } 
    }
    
    protected void addThrowsUrls(WebClient webClient, URL[] urls) throws IOException {
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            WebResponse webResponse = mock(WebResponse.class);
            when(webResponse.openReaderSource()).thenThrow(new IOException());
            when(webClient.openWebResponse(eq(url))).thenReturn(webResponse);    
        } 
    }
}
