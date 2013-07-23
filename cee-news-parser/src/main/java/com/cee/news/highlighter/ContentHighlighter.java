package com.cee.news.highlighter;

import java.io.IOException;
import java.io.Writer;

import com.cee.news.model.Article;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.net.WebResponse;

public interface ContentHighlighter {
	
	public static class Settings {
		
		public static final boolean DEFAULT_HIGHLIGHT_CONTENT_BLOCK = true;
		
		private boolean highlightContentBlock = DEFAULT_HIGHLIGHT_CONTENT_BLOCK;
		
		public boolean isHighlightContentBlock() {
			return highlightContentBlock;
		}

		public void setHighlightContentBlock(boolean highlightContentBlock) {
			this.highlightContentBlock = highlightContentBlock;
		}

		public static final String DEFAULT_CONTENT_BLOCK_START = "<span style=\"color: #000000; background-color: #fdff66\">";
		
		private String contentBlockStart = DEFAULT_CONTENT_BLOCK_START;
				
		public String getContentBlockStart() {
			return contentBlockStart;
		}

		public void setContentBlockStart(String contentBlockStart) {
			this.contentBlockStart = contentBlockStart;
		}

		private static final String DEFAULT_CONTENT_BLOCK_END = "</span>";
		
		private String contentBlockEnd = DEFAULT_CONTENT_BLOCK_END;

		public String getContentBlockEnd() {
			return contentBlockEnd;
		}

		public void setContentBlockEnd(String contentBlockEnd) {
			this.contentBlockEnd = contentBlockEnd;
		}
	}
	
	public void highlightContent(Writer output, WebResponse webResponse, Article article, Settings settings) throws ParserException, IOException;

}
