package org.cee.parser.impl;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.cee.net.WebClient;
import org.cee.net.impl.DefaultHttpClientFactory;
import org.cee.net.impl.DefaultWebClient;
import org.cee.net.impl.HttpClientFactory;
import org.cee.net.impl.ReaderFactory;
import org.cee.net.impl.XmlStreamReaderFactory;
import org.cee.parser.impl.html.LinkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SiteCrawler extends XmlReaderProvider {
    
    private static final Logger LOG = LoggerFactory.getLogger(SiteCrawler.class);

    private final URL siteLocation;
    
    private final boolean remainWithinDomain;
    
    private final int maxDepth;
    
    private final ContentHandler[] contentHandlers;
    
    private final ExecutorService executorService = new ForkJoinPool(64);
    
    private final Set<URL> visitedLinks = new HashSet<URL>();
    
    public SiteCrawler(URL siteLocation, boolean remainWithinDomain, int maxDepth, SaxXmlReaderFactory xmlReaderFactory, ContentHandler... contentHandlers) {
        super(xmlReaderFactory);
        this.siteLocation = siteLocation;
        this.remainWithinDomain = remainWithinDomain;
        this.maxDepth = maxDepth;
        this.contentHandlers = contentHandlers;
    }
    
    private Set<URL> crawlPage(WebClient webClient, URL location) {
        XMLReader xmlReader = createXmlReader();
        LinkHandler linkHandler = new LinkHandler(location);
        ContentHandler contentHandler = null;
        if (contentHandlers == null) {
            contentHandler = linkHandler;
        } else {
            contentHandler = new TeeContentHandler(contentHandlers).add(linkHandler);
        }
        xmlReader.setContentHandler(contentHandler);
        try(Reader reader = webClient.openWebResponse(location, false).openReader()) {
            xmlReader.parse(new InputSource(reader));
        } catch (IOException e) {
            LOG.warn("Could not open link resource", e);
        } catch (SAXException e) {
            LOG.warn("Could not parse link resource", e);
        }
        LOG.debug("Visited location {}.", location);
        System.out.println("Visited location: " + location);
        return linkHandler.getLinks();
    }
    
    private boolean follow(URL link) {
        if (remainWithinDomain) {
            
        }
        return false;
    }
    
    public void crawl(final WebClient webClient) throws InterruptedException {
        int depth = 0;
        Set<URL> linksToBeProcessed = new LinkedHashSet<>();
        linksToBeProcessed.add(siteLocation);
        while(!linksToBeProcessed.isEmpty()) {
            List<Callable<Set<URL>>> tasks = new ArrayList<Callable<Set<URL>>>();
            for (final URL location : linksToBeProcessed) {
                tasks.add(new Callable<Set<URL>>() {
                    
                    @Override
                    public Set<URL> call() {
                        return crawlPage(webClient, location);
                    }
                });
                this.visitedLinks.add(location);
            }
            List<Future<Set<URL>>> results = executorService.invokeAll(tasks);
            linksToBeProcessed.clear();
            for (Future<Set<URL>> result : results) {
                try {
                    Set<URL> pageResult = result.get();
                    for (URL link : pageResult) {
                        if (!this.visitedLinks.contains(link) && follow(link)) {
                            linksToBeProcessed.add(link);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            depth++;
            if (depth > this.maxDepth) {
                break;
            }
            
        }
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: SiteCrawler URL");
            return;
        }
        try {
            URL location = new URL(args[0]);
            SaxXmlReaderFactory xmlReaderFactory = new TagsoupXmlReaderFactory();
            HttpClientFactory httpClientFactory = new DefaultHttpClientFactory();
            ReaderFactory readerFactory = new XmlStreamReaderFactory();
            WebClient webClient = new DefaultWebClient(httpClientFactory, readerFactory);
            new SiteCrawler(location, true, 1, xmlReaderFactory, (ContentHandler[])null).crawl(webClient);
        } catch (MalformedURLException e) {
            System.out.println("Wrong URL format:" + e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}