package org.cee.parser.impl;

import java.util.ArrayList;
import java.util.List;

import org.cee.parser.ArticleParser.Settings;
import org.cee.store.article.ContentExtractionMetaData;
import org.cee.store.article.TextBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ExtractorBase;
import de.l3s.boilerpipe.filters.english.NumWordsRulesClassifier;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Internal class implementing the core content extraction processes using
 * the boilerplate API.
 * @author andreasbehnke
 */
class BoilerpipeContentExtractor {
	
	private final static class ArticleExtractor extends ExtractorBase {
	    
		private static final double MAX_LEVENSHTEIN_DISTANCE_HTML_TITLE_MATCH = 0.4;

		private static final double MAX_LEVENSHTEIN_DISTANCE_RSS_TITLE_MATCH = 0.2;

		private final BoilerpipeFilter terminatingBlocksFinder;
	    
	    private final String htmlTitle;
	    
	    private ArticleExtractor(String htmlTitle) {
	    	this.htmlTitle = htmlTitle;
	    	List<String> matches = new ArrayList<String>();
	    	//english stopwords
	    	matches.add("comments");
	    	matches.add("comment");
	    	matches.add("users responded in");
	    	matches.add("please rate this");
	    	matches.add("what you think...");
	    	matches.add("reader views");
	    	matches.add("have your say");
	    	
	    	//german stopwords
	    	matches.add("kommentieren");
	    	matches.add("diesen artikel...");
	    	matches.add("Kommentare zu diesem Artikel");
	    	
	    	terminatingBlocksFinder = new ContainsTextFilter(
	    			DefaultLabels.INDICATES_END_OF_TEXT, 
	    			matches, 
	    			6, 
	    			true);
	    	}

	    @Override
	    public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
	    	BoilerpipeFilter titleFinderRSS = new DocumentTitleMatchFilter(DefaultLabels.TITLE, doc.getTitle(), MAX_LEVENSHTEIN_DISTANCE_RSS_TITLE_MATCH, false);
	    	BoilerpipeFilter titleFinderHTML = new DocumentTitleMatchFilter(DefaultLabels.TITLE, htmlTitle, MAX_LEVENSHTEIN_DISTANCE_HTML_TITLE_MATCH, false);
	    	
	    	boolean changed = terminatingBlocksFinder.process(doc);
	    	boolean foundTitle = titleFinderRSS.process(doc);
	    	foundTitle |= titleFinderHTML.process(doc);
	    	if (!foundTitle) {
	    		LOG.warn("Title not found in content of article {}", doc.getTitle());
	    		return false;
	    	}
	    	changed |= foundTitle; 
	    	changed |= NumWordsRulesClassifier.INSTANCE.process(doc);
	    	changed |= FindContentAfterTitleFilter.INSTANCE.process(doc);
	    	return changed;
	    }
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(BoilerpipeContentExtractor.class);

	public List<TextBlock> extractContent(TextDocument input, String titleHint, Settings settings) throws BoilerpipeProcessingException {
		List<TextBlock> content = new ArrayList<>();
		if (!new ArticleExtractor(titleHint).process(input)) {
    		return null;
    	}
        for (de.l3s.boilerpipe.document.TextBlock block : input.getTextBlocks()) {
        	if (!block.isContent() && settings.isFilterContentBlocks()) {
        		continue;
        	}
        	String paragraph = block.getText();
        	org.cee.store.article.TextBlock internalBlock = new org.cee.store.article.TextBlock(paragraph);
        	if (settings.isProvideMetaData()) {
        		ContentExtractionMetaData metaData = ContentExtractionMetaDataFactory.INSTANCE.create(content.size(), block);
        		internalBlock.setMetaData(metaData);
        	}
        	content.add(internalBlock);
        }
		return content;
	}
	
}
