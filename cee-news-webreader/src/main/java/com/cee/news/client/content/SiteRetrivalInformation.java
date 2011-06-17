package com.cee.news.client.content;

import com.cee.news.model.Site;

public class SiteRetrivalInformation {

	public enum SiteRetrivalState {
		ok, malformedUrl, ioError, parserError
	}

	private Site site;

	private SiteRetrivalState state;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public SiteRetrivalState getState() {
		return state;
	}

	public void setState(SiteRetrivalState state) {
		this.state = state;
	}
}
