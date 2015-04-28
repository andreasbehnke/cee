package org.cee.site.crawler;

import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.StopWatch;
import org.cee.crawler.FollowConstraint;
import org.cee.crawler.SiteCrawler;
import org.cee.crawler.SiteCrawlerCallback;
import org.cee.net.WebClient;
import org.cee.net.WebResponse;
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

    private final Set<URL> visitedLinks = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    private final WebClient webClient; 
    
    public SiteCrawlerImpl(WebClient webClient, SaxXmlReaderFactory xmlReaderFactory) {
        super(xmlReaderFactory);
        this.webClient = webClient;
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
    
    private Set<URL> crawlPage(WebResponse webResponse, SiteCrawlerCallback callback) {
    	Set<URL> links = null;
    	StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try(Reader reader = webResponse.openReader()) {
        	XMLReader xmlReader = createXmlReader();
        	URL location = webResponse.getLocation();
            LinkHandler linkHandler = new LinkHandler(location);
            ContentHandler contentHandler = null;
            ContentHandler[] contentHandlers = notifyPageStart(location, callback);
            if (contentHandlers == null) {
                contentHandler = linkHandler;
            } else {
                contentHandler = new TeeContentHandler(contentHandlers).add(linkHandler);
            }
            xmlReader.setContentHandler(contentHandler);
            xmlReader.parse(new InputSource(reader));
            notifyPageFinished(location, callback, contentHandlers);
            links = linkHandler.getLinks();
            stopWatch.stop();
            LOG.info("duration: {}, links: {}, location: {}", new Object[]{stopWatch, links.size(), location});
        } catch (Exception e) {
        	LOG.warn("error ({}) processing location {}", new Object[]{ e.getMessage(), webResponse.getOriginalLocation() } );
        	links = Collections.emptySet();
        }
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
    
    private <T> void waitForAllFutures(List<Future<T>> results) throws InterruptedException {
    	for (Future<T> future : results) {
    		if (!future.isDone()) {
                try {
                	future.get();
                } catch (CancellationException ignore) {
                } catch (ExecutionException ignore) {
                }
            }
		}
    }
    
    @Override
    public void crawl(URL siteLocation, final SiteCrawlerCallback callback, FollowConstraint... followConstraints) throws InterruptedException {
        int depth = 0;
        Set<URL> linksToBeProcessed = new LinkedHashSet<>();
        linksToBeProcessed.add(siteLocation);
        while(!linksToBeProcessed.isEmpty()) {
        	List<Future<Set<URL>>> results = new ArrayList<>();
            for (final URL location : linksToBeProcessed) {
            	try {
            		results.add(this.webClient.processWebResponse(location, false, (wr) -> { return crawlPage(wr, callback); } ) );
            	} catch(Exception e) {
            		LOG.error("Could not add response processor to processing queue", e);
            	}
            	this.visitedLinks.add(location);
            }
            waitForAllFutures(results);
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
    }
}