package com.cee.news.parser.impl;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ExtractorBase;
import de.l3s.boilerpipe.filters.english.IgnoreBlocksAfterContentFilter;
import de.l3s.boilerpipe.filters.english.KeepLargestFulltextBlockFilter;
import de.l3s.boilerpipe.filters.english.NumWordsRulesClassifier;
import de.l3s.boilerpipe.filters.english.TerminatingBlocksFinder;
import de.l3s.boilerpipe.filters.heuristics.BlockProximityFusion;
import de.l3s.boilerpipe.filters.heuristics.DocumentTitleMatchClassifier;
import de.l3s.boilerpipe.filters.heuristics.ExpandTitleToContentFilter;
import de.l3s.boilerpipe.filters.simple.BoilerplateBlockFilter;

public class ArticleExtractor extends ExtractorBase {
    
    public static final ArticleExtractor INSTANCE = new ArticleExtractor();

    public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
        return TerminatingBlocksFinder.INSTANCE.process(doc)
                | new DocumentTitleMatchClassifier(doc.getTitle()).process(doc)
                | NumWordsRulesClassifier.INSTANCE.process(doc)
                | IgnoreBlocksAfterContentFilter.DEFAULT_INSTANCE.process(doc)
                | BlockProximityFusion.MAX_DISTANCE_1.process(doc)
                | BoilerplateBlockFilter.INSTANCE.process(doc)
                | BlockProximityFusion.MAX_DISTANCE_1_CONTENT_ONLY.process(doc)
                | KeepLargestFulltextBlockFilter.INSTANCE.process(doc)
                | ExpandTitleToContentFilter.INSTANCE.process(doc);
    }
}
