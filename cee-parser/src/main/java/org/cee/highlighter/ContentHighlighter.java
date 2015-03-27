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

import java.io.IOException;
import java.io.Writer;
import java.net.URL;

import org.cee.net.WebResponse;
import org.cee.parser.ParserException;
import org.cee.store.article.Article;

public interface ContentHighlighter {
	
	public static class Settings {
		
		/**
		 * Content of the current entity, it depends on the template
		 */
		public static final String PARAMETER_CONTENT = "CONTENT";
		/**
		 * The ID of the referenced text block
		 */
		public static final String PARAMETER_ID = "ID";
		/**
		 * Random string which is a constant value for the complete parsing process.
		 * Use this namespace to create unique keys for HTML id elements. 
		 */
		public static final String PARAMETER_NAMESPACE = "NAMESPACE";
		
		private String headerTemplate;
		private boolean highlightContentBlock;
		private String contentBlockTemplate;
		private URL baseUrl;
		private boolean rewriteUrls;
		private boolean showBlockMetadata;
		private String metadataTemplate;
		private String metadataContainerTemplate;
		private String metadataIconTemplate;
		
		public String getHeaderTemplate() {
			return headerTemplate;
		}
		public void setHeaderTemplate(String headerTemplate) {
			this.headerTemplate = headerTemplate;
		}
		public boolean isHighlightContentBlock() {
			return highlightContentBlock;
		}
		public void setHighlightContentBlock(boolean highlightContentBlock) {
			this.highlightContentBlock = highlightContentBlock;
		}
		public String getContentBlockTemplate() {
			return contentBlockTemplate;
		}
		public void setContentBlockTemplate(String contentBlockTemplate) {
			this.contentBlockTemplate = contentBlockTemplate;
		}
		public URL getBaseUrl() {
			return baseUrl;
		}
		public void setBaseUrl(URL baseUrl) {
			this.baseUrl = baseUrl;
		}
		public boolean isRewriteUrls() {
			return rewriteUrls;
		}
		public void setRewriteUrls(boolean rewriteUrls) {
			this.rewriteUrls = rewriteUrls;
		}
		public boolean isShowBlockMetadata() {
			return showBlockMetadata;
		}
		public void setShowBlockMetadata(boolean showBlockMetadata) {
			this.showBlockMetadata = showBlockMetadata;
		}
		public String getMetadataTemplate() {
			return metadataTemplate;
		}
		public void setMetadataTemplate(String metadataTemplate) {
			this.metadataTemplate = metadataTemplate;
		}
		public String getMetadataContainerTemplate() {
			return metadataContainerTemplate;
		}
		public void setMetadataContainerTemplate(String metadataContainerTemplate) {
			this.metadataContainerTemplate = metadataContainerTemplate;
		}
		public String getMetadataIconTemplate() {
			return metadataIconTemplate;
		}
		public void setMetadataIconTemplate(String metadataIconTemplate) {
			this.metadataIconTemplate = metadataIconTemplate;
		}
		
		public Settings() {}
		
		public Settings(String headerTemplate, boolean highlightContentBlock, String contentBlockTemplate, URL baseUrl, boolean rewriteUrls, boolean showBlockMetadata, String metadataTemplate, String metadataContainerTemplate, String metadataIconTemplate) {
	        this.headerTemplate = headerTemplate;
	        this.highlightContentBlock = highlightContentBlock;
	        this.contentBlockTemplate = contentBlockTemplate;
	        this.baseUrl = baseUrl;
	        this.rewriteUrls = rewriteUrls;
	        this.showBlockMetadata = showBlockMetadata;
	        this.metadataTemplate = metadataTemplate;
	        this.metadataContainerTemplate = metadataContainerTemplate;
	        this.metadataIconTemplate = metadataIconTemplate;
        }
	}
	
	public void highlightContent(Writer output, WebResponse webResponse, Article article, Settings settings) throws ParserException, IOException;

}
