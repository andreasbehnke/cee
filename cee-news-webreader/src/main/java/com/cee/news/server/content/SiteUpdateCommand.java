package com.cee.news.server.content;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cee.news.model.Site;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.impl.SiteReader;
import com.cee.news.parser.net.WebClient;
import com.cee.news.store.StoreException;

/**
 * Work item which uses a {@link SiteReader} internally
 */
public class SiteUpdateCommand extends AbstractCommand {
	
	private static final Logger log = LoggerFactory.getLogger(SiteUpdateCommand.class);
	
	private final WebClient webClient;
	
	private final SiteReader siteReader;
	
	private final Site site;

	public SiteUpdateCommand(WebClient webClient, SiteReader siteReader, Site site) {
	    this.webClient = webClient;
	    this.siteReader = siteReader;
	    this.site = site;
    }

	@Override
	protected void runInternal() {
		try {
			log.info("Start updating site {} at location {}", site.getName(), site.getLocation());
			siteReader.update(webClient, site);
			log.info("Finished updating site {} at location {}", site.getName(), site.getLocation());
		} catch (ParserException e) {
			log.error("Could not parse articles of site", e);
			throw new RuntimeException(e);
		} catch (StoreException e) {
			log.error("Could not store articles of site", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("Could not read site or site article", e);
			throw new RuntimeException(e);
		}
	}
}
