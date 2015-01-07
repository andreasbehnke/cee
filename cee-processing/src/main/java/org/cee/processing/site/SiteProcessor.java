package org.cee.processing.site;

import java.io.IOException;
import java.net.MalformedURLException;

import org.cee.client.site.FeedData;
import org.cee.client.site.SiteConverter;
import org.cee.client.site.SiteData;
import org.cee.client.site.SiteData.SiteRetrivalState;
import org.cee.news.model.Feed;
import org.cee.news.model.Site;
import org.cee.parser.ParserException;
import org.cee.parser.SiteReader;
import org.cee.parser.net.WebClient;
import org.cee.parser.net.WebClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteProcessor.class);
	
	private static final String MALFORMED_URL = "The URL {} is malformed";
	
	private static final String IO_ERROR = "Resource {} could not be loaded, an IO error occured.";
	
	private static final String PARSER_ERROR = "Resource {} could not be loaded, a parser error occured";
	
	private WebClientFactory webClientFactory;
	
	private SiteReader siteReader;
	
	public void setWebClientFactory(WebClientFactory webClientFactory) {
		this.webClientFactory = webClientFactory;
	}
	
	public void setSiteReader(SiteReader siteReader) {
		this.siteReader = siteReader;
	}
	
	private WebClient createWebClient() {
		if (webClientFactory == null) {
			throw new IllegalStateException("Property webClientFactory has not been set");
		}
		return webClientFactory.createWebClient();
	}

	public SiteData retrieveSiteData(String location) {
		SiteData info = new SiteData();
		info.setIsNew(true);
		try {
			Site site = siteReader.readSite(createWebClient(), location);
			info = SiteConverter.createFromSite(site);
			info.setState(SiteRetrivalState.ok);
		} catch(MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			LOG.warn(MALFORMED_URL, location);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.warn(IO_ERROR, location);
		} catch (ParserException e) {
			info.setState(SiteRetrivalState.parserError);
			LOG.warn(PARSER_ERROR, location);
		}
		return info;
	}
	
	public FeedData retrieveFeedData(String location) {
		FeedData info = new FeedData();
		info.setIsNew(true);
		try {
			Feed feed = siteReader.readFeed(createWebClient(), location);
			info = SiteConverter.createFromFeed(feed);
			info.setState(SiteRetrivalState.ok);
		} catch(MalformedURLException e) {
			info.setState(SiteRetrivalState.malformedUrl);
			LOG.warn(MALFORMED_URL, location);
		} catch (IOException e) {
			info.setState(SiteRetrivalState.ioError);
			LOG.warn(IO_ERROR, location);
		} catch (ParserException e) {
			info.setState(SiteRetrivalState.parserError);
			LOG.warn(PARSER_ERROR, location);
		}
		return info;
	}
}
