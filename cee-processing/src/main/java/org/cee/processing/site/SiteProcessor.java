package org.cee.processing.site;

/*
 * #%L
 * Content Extraction Engine - Content Processing
 * %%
 * Copyright (C) 2013 - 2015 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;
import java.net.MalformedURLException;

import org.cee.client.site.FeedData;
import org.cee.client.site.SiteConverter;
import org.cee.client.site.SiteData;
import org.cee.client.site.SiteData.SiteRetrivalState;
import org.cee.parser.ParserException;
import org.cee.parser.SiteReader;
import org.cee.store.site.Feed;
import org.cee.store.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(SiteProcessor.class);
	
	private static final String MALFORMED_URL = "The URL {} is malformed";
	
	private static final String IO_ERROR = "Resource {} could not be loaded, an IO error occured.";
	
	private static final String PARSER_ERROR = "Resource {} could not be loaded, a parser error occured";
	
	private SiteReader siteReader;
	
	public void setSiteReader(SiteReader siteReader) {
		this.siteReader = siteReader;
	}
	
	public SiteData retrieveSiteData(String location) {
		SiteData info = new SiteData();
		info.setIsNew(true);
		try {
			Site site = siteReader.readSite(location);
			info = SiteConverter.createFromSite(site, true);
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
			Feed feed = siteReader.readFeed(location);
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
