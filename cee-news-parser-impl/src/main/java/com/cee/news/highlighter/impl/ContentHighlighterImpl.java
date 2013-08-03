package com.cee.news.highlighter.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.cee.news.highlighter.ContentHighlighter;
import com.cee.news.model.Article;
import com.cee.news.model.TextBlock;
import com.cee.news.parser.ArticleParser;
import com.cee.news.parser.ParserException;
import com.cee.news.parser.impl.ArticleReader;
import com.cee.news.parser.impl.SaxXmlReaderFactory;
import com.cee.news.parser.impl.XmlReaderProvider;
import com.cee.news.parser.net.WebResponse;

public class ContentHighlighterImpl extends XmlReaderProvider implements ContentHighlighter {
	
	private final static Logger LOG = LoggerFactory.getLogger(ContentHighlighterImpl.class);
	
	private final static String ISSUE_COULD_NOT_PARSE_ARTICLE = "Article could not be parsed";
	
	private ArticleReader articleReader;
	
	private TemplateCache templateCache;
	
	public ContentHighlighterImpl() {}
	
	public ContentHighlighterImpl(ArticleReader articleReader, SaxXmlReaderFactory xmlReaderFactory, TemplateCache templateCache) {
		super(xmlReaderFactory);
	    this.articleReader = articleReader;
	    this.templateCache = templateCache;
    }
	
	public void setArticleReader(ArticleReader articleReader) {
	    this.articleReader = articleReader;
    }
	
	public void setTemplateCache(TemplateCache templateCache) {
	    this.templateCache = templateCache;
    }

	@Override
    public void highlightContent(Writer output, WebResponse response, Article article,  Settings settings) throws ParserException, IOException {
	    List<String> issues = new ArrayList<String>();
	    ArticleParser.Settings parserSettings = new ArticleParser.Settings();
	    parserSettings.setProvideMetaData(true);
	    parserSettings.setFilterContentBlocks(false);
	    article = articleReader.readArticle(response, article, parserSettings);
	    
		List<TextBlock> blocks = null;
	    if (article == null) {
	    	blocks = new ArrayList<TextBlock>();
	    	LOG.warn(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    	issues.add(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    } else {
	    	blocks = article.getContent();
	    }
	    
	    XMLReader xmlReader = createXmlReader();
	    xmlReader.setContentHandler(new HighlightHandler(blocks, output, settings, templateCache));
	    Reader reader = response.openReaderSource().getReader();
	    try {
	    	xmlReader.parse(new InputSource(reader));
	    } catch (SAXException e) {
	        throw new ParserException(e);
        } finally {
	    	IOUtils.closeQuietly(reader);
	    }
    }
}
