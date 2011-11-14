package com.cee.news.parser.impl;

import java.net.ProxySelector;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

public class HttpClientFactory {

	public static HttpClient createHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
		    client.getConnectionManager().getSchemeRegistry(),
		    ProxySelector.getDefault()
		);
		client.setRoutePlanner(routePlanner);
		return client;
	}
}
