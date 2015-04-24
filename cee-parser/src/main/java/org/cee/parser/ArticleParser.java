package org.cee.parser;

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
import java.io.Reader;

import org.cee.store.article.Article;
import org.cee.store.article.TextBlock;

public interface ArticleParser {
	
	public static class Settings {
		
		public static boolean DEFAULT_PROVIDE_META_DATA = false;
		
		private boolean provideMetaData = DEFAULT_PROVIDE_META_DATA;
		
		public boolean isProvideMetaData() {
			return provideMetaData;
		}

		public void setProvideMetaData(boolean provideMetaData) {
			this.provideMetaData = provideMetaData;
		}

		public static boolean DEFAULT_FILTER_CONTENT_BLOCKS = true;
		
		private boolean filterContentBlocks = DEFAULT_FILTER_CONTENT_BLOCKS;

		public boolean isFilterContentBlocks() {
			return filterContentBlocks;
		}

		public void setFilterContentBlocks(boolean filterContentBlocks) {
			this.filterContentBlocks = filterContentBlocks;
		}
	}
    
    /**
     * Parses the article content page and adds the content {@link TextBlock}s to the article.
     * @return The article with new content added or null if the article content has a poor quality and could not be parsed
     * @throws ParserException If the source could not be parsed
     * @throws IOException If an IO error occurred
     */
    Article parse(Reader reader, Article article, Settings settings) throws ParserException, IOException;

}