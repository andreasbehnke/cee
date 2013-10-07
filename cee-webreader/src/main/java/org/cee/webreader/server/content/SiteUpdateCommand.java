package org.cee.webreader.server.content;

import java.io.IOException;

import org.cee.news.model.Site;
import org.cee.news.store.StoreException;
import org.cee.parser.ParserException;
import org.cee.parser.SiteReader;
import org.cee.parser.net.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		} catch (StoreException e) {
			log.error("Could not store articles of site", e);
			throw new RuntimeException(e);
		}
	}
}
