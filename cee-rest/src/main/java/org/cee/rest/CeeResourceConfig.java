package org.cee.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class CeeResourceConfig extends ResourceConfig {

	public CeeResourceConfig() {
		register(RequestContextFilter.class);
		packages("org.cee.rest");
	}
}
