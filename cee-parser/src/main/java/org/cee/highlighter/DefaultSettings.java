package org.cee.highlighter;

/*
 * #%L
 * Content Extraction Engine - News Parser
 * %%
 * Copyright (C) 2013 Andreas Behnke
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

import java.net.URL;

import org.cee.highlighter.ContentHighlighter.Settings;

public class DefaultSettings extends Settings {
	
	public static final String DEFAULT_HEADER_TEMPLATE = "classpath:/org/cee/highlighter/defaultHeaderTemplate.html";
	public static final boolean DEFAULT_HIGHLIGHT_CONTENT_BLOCK = true;
	public static final String DEFAULT_CONTENT_BLOCK_TEMPLATE = "classpath:/org/cee/highlighter/defaultContentBlockTemplate.html";
	public static final boolean DEFAULT_REWRITE_URLS = true;
	public static final boolean DEFAULT_SHOW_BLOCK_METADATA = true;
	public static final String DEFAULT_METADATA_TEMPLATE = "classpath:/org/cee/highlighter/defaultMetadataTemplate.html";
	public static final String DEFAULT_METADATA_CONTAINER_TEMPLATE = "classpath:/org/cee/highlighter/defaultMetadataContainerTemplate.html";
	public static final String DEFAULT_METADATA_ICON_TEMPLATE = "classpath:/org/cee/highlighter/defaultMetadataIconTemplate.html";
	
	private DefaultSettings() {}
	
	public static Settings createDefaultSettings(URL baseUrl) {
		return  new Settings(
				DEFAULT_HEADER_TEMPLATE, 
				DEFAULT_HIGHLIGHT_CONTENT_BLOCK, 
				DEFAULT_CONTENT_BLOCK_TEMPLATE, 
				baseUrl, 
				DEFAULT_REWRITE_URLS, 
				DEFAULT_SHOW_BLOCK_METADATA, 
				DEFAULT_METADATA_TEMPLATE, 
				DEFAULT_METADATA_CONTAINER_TEMPLATE, 
				DEFAULT_METADATA_ICON_TEMPLATE);
	}
}
