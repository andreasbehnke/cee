package com.cee.news.server.content;

import java.util.List;

import com.cee.news.client.content.SiteUpdateService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SiteUpdateServiceImpl extends RemoteServiceServlet implements SiteUpdateService {

	@Override
	public int updateSites(List<String> sites) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateQueueSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
