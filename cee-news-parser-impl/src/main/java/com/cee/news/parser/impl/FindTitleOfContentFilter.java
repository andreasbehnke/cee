package com.cee.news.parser.impl;

import java.util.HashMap;
import java.util.Map;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Find title which has the most content blocks.
 * Removes all text blocks, which are not in between this title block and 
 * the next title block or block labeled "INDICATES_END_OF_TEXT".
 */
public class FindTitleOfContentFilter implements BoilerpipeFilter {
	
	public final static FindTitleOfContentFilter INSTANCE = new FindTitleOfContentFilter();

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		Map<Integer, Integer> titleContentBlockCount = new HashMap<Integer, Integer>(); 
		
		//find text box labeled "TITLE" which has the most content blocks
		int titleIndex = -1;
		int i = 0;
		for (TextBlock tb : doc.getTextBlocks()) {
			if (tb.hasLabel(DefaultLabels.TITLE)) {
				titleIndex = i;
				titleContentBlockCount.put(titleIndex, 0);
			} else if (titleIndex > -1 && i - titleIndex < 10 && tb.isContent()) {
				int count = titleContentBlockCount.get(titleIndex);
				count++;
				titleContentBlockCount.put(titleIndex, count);
			}
			i++;
		}
		if (titleContentBlockCount.size() == 0) {
			return false;
		}
		
		//find title with max content blocks
		int maxBlocks = 0;
		int bestTitle = -1;
		for (Map.Entry<Integer, Integer> contentCount : titleContentBlockCount.entrySet()) {
			int count = contentCount.getValue().intValue(); 
	        if (count >= maxBlocks) {
	        	maxBlocks = contentCount.getValue().intValue();
	        	bestTitle = contentCount.getKey();
	        }
        }
		
		//mark all text blocks before title as no content
		for (TextBlock tb : doc.getTextBlocks().subList(0, bestTitle + 1)) {
			tb.setIsContent(false);
		}
		//find first text block labeled "INDICATES_END_OF_TEXT" of "TITLE
		boolean endOfText = false;
		for (TextBlock tb : doc.getTextBlocks().subList(bestTitle + 1, doc.getTextBlocks().size())) {
			if (tb.hasLabel(DefaultLabels.INDICATES_END_OF_TEXT) || tb.hasLabel(DefaultLabels.TITLE)) {
				endOfText = true;
			}
			if (endOfText) {
				tb.setIsContent(false);
			}
		}
		return true;
	}

}
