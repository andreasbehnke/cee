package com.cee.news.parser.impl;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.labels.DefaultLabels;

/**
 * Removes all text blocks, which are not in between the last text box labeled "TITLE" and the first text box labeled "INDICATES_END_OF_TEXT".
 */
public class TitleToEndOfContentFilter implements BoilerpipeFilter {
	
	public final static TitleToEndOfContentFilter INSTANCE = new TitleToEndOfContentFilter();

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		//find first text box labeled "TITLE"
		int titleIndex = -1;
		int i = 0;
		boolean expectedTitle = false;
		int blockCnt = 0;
		int wordCnt = 0;
		for (TextBlock tb : doc.getTextBlocks()) {
			if (tb.hasLabel(DefaultLabels.TITLE)) {
				expectedTitle = true;
				blockCnt = 0;
				wordCnt = 0;
				titleIndex = i;
			} else if (expectedTitle) {//the next three blocks must have at least 40 words
				wordCnt += tb.getNumWords();
				blockCnt++;
				if (blockCnt == 3) {
					expectedTitle = false;
					if (wordCnt > 25) {
						break;
					} else {
						titleIndex = -1;
					}
				}
			}
			i++;
		}
		if (titleIndex < 0) {
			return false;
		}
		//mark all text blocks before title as no content
		for (TextBlock tb : doc.getTextBlocks().subList(0, titleIndex + 1)) {
			tb.setIsContent(false);
		}
		//find first text block labeled "INDICATES_END_OF_TEXT"
		boolean endOfText = false;
		for (TextBlock tb : doc.getTextBlocks().subList(titleIndex + 1, doc.getTextBlocks().size())) {
			if (tb.hasLabel(DefaultLabels.INDICATES_END_OF_TEXT)) {
				endOfText = true;
			}
			if (endOfText) {
				tb.setIsContent(false);
			}
		}
		return true;
	}

}
