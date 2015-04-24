package org.cee.site.crawler;

import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.cee.crawler.FollowConstraint;
import org.cee.crawler.SiteCrawler;
import org.cee.crawler.SiteCrawlerCallback;
import org.cee.net.WebClient;
import org.cee.parser.impl.SaxXmlReaderFactory;
import org.cee.parser.impl.TeeContentHandler;
import org.cee.parser.impl.XmlReaderProvider;
import org.cee.parser.impl.html.LinkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SiteCrawlerImpl extends XmlReaderProvider implements SiteCrawler {
    
    private static final Logger LOG = LoggerFactory.getLogger(SiteCrawlerImpl.class);

    private ExecutorService executorService;
    
    private final Set<URL> visitedLinks = new HashSet<URL>();
    
    public SiteCrawlerImpl(SaxXmlReaderFactory xmlReaderFactory) {
        super(xmlReaderFactory);
    }
    
    private ContentHandler[] notifyPageStart(URL location, SiteCrawlerCallback callback) {
        if (callback != null) {
            return callback.onPageStart(location);
        } else {
            return null;
        }
    }
    
    private void notifyPageFinished(URL location, SiteCrawlerCallback callback, ContentHandler... contentHandlers) {
        if (callback != null) {
            callback.onPageFinished(location, contentHandlers);
        }
    }
    
    private Set<URL> crawlPage(WebClient webClient, URL location, SiteCrawlerCallback callback) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        XMLReader xmlReader = createXmlReader();
        LinkHandler linkHandler = new LinkHandler(location);
        ContentHandler contentHandler = null;
        ContentHandler[] contentHandlers = notifyPageStart(location, callback);
        if (contentHandlers == null) {
            contentHandler = linkHandler;
        } else {
            contentHandler = new TeeContentHandler(contentHandlers).add(linkHandler);
        }
        xmlReader.setContentHandler(contentHandler);
        try(Reader reader = webClient.openWebResponse(location, false).openReader()) {
            xmlReader.parse(new InputSource(reader));
        } catch (Exception e) {
            LOG.warn("Could not open link resource for " + location, e);
        }
        notifyPageFinished(location, callback, contentHandlers);
        Set<URL> links = linkHandler.getLinks();
        stopWatch.stop();
        LOG.debug("Visiting location {} took {} and found {} new links.", new Object[]{location, stopWatch, links.size()});
        return links;
    }
    
    private boolean follow(URL link, int depth, FollowConstraint... followConstraints) {
        for (FollowConstraint followConstraint : followConstraints) {
            if (!followConstraint.follow(depth, link)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void crawl(final WebClient webClient, URL siteLocation, final SiteCrawlerCallback callback, FollowConstraint... followConstraints) throws InterruptedException {
        executorService = Executors.newFixedThreadPool(20);
        int depth = 0;
        Set<URL> linksToBeProcessed = new LinkedHashSet<>();
        linksToBeProcessed.add(siteLocation);
        try {
            while(!linksToBeProcessed.isEmpty()) {
                List<Callable<Set<URL>>> tasks = new ArrayList<Callable<Set<URL>>>();
                for (final URL location : linksToBeProcessed) {
                    tasks.add(new Callable<Set<URL>>() {
                        
                        @Override
                        public Set<URL> call() {
                            return crawlPage(webClient, location, callback);
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
                            if (!this.visitedLinks.contains(link) && follow(link, depth, followConstraints)) {
                                linksToBeProcessed.add(link);
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
                depth++;
                
            }
        } finally {
            executorService.shutdownNow();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}