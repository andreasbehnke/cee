package org.cee.highlighter.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.cee.highlighter.ContentHighlighter;
import org.cee.news.model.Article;
import org.cee.parser.ArticleParser;
import org.cee.parser.ArticleReader;
import org.cee.parser.ParserException;
import org.cee.parser.impl.SaxXmlReaderFactory;
import org.cee.parser.impl.XmlReaderProvider;
import org.cee.parser.net.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

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
    public void highlightContent(Writer output, WebResponse response, Article article,  Settings settings) throws MalformedURLException, IOException, ParserException {
	    List<String> issues = new ArrayList<String>();
	    ArticleParser.Settings parserSettings = new ArticleParser.Settings();
	    parserSettings.setProvideMetaData(true);
	    parserSettings.setFilterContentBlocks(false);
	    try {
	    	article = articleReader.readArticle(response, article, parserSettings);
	    } catch(ParserException pe) {
	    	LOG.warn(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    	issues.add(ISSUE_COULD_NOT_PARSE_ARTICLE);
	    }
		XMLReader xmlReader = createXmlReader();
	    HighlightWriter writer = new HighlightWriter(output, templateCache, settings);
	    xmlReader.setContentHandler(new HighlightHandler(article.getContent(), issues, writer, settings));
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
