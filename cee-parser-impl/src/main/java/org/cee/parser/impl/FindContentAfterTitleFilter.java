package org.cee.parser.impl;

/*
 * #%L
 * Content Extraction Engine - News Parser Implementations
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class FindContentAfterTitleFilter implements BoilerpipeFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(FindContentAfterTitleFilter.class); 
	
	public static final FindContentAfterTitleFilter INSTANCE = new FindContentAfterTitleFilter();

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
			} else if (titleIndex > -1 && i - titleIndex < 20 && tb.isContent()) {
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
		//sort by text block index
		int maxBlocks = 0;
		int bestTitle = -1;
		List<Map.Entry<Integer, Integer>> entries = new ArrayList<Map.Entry<Integer,Integer>>(titleContentBlockCount.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {
			@Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
	            return o1.getKey().compareTo(o2.getKey());
            }
		});
		
		//find title text block with max content blocks
		for (Map.Entry<Integer, Integer> contentCount : entries) {
			int count = contentCount.getValue().intValue(); 
	        if (count >= maxBlocks) {
	        	maxBlocks = contentCount.getValue().intValue();
	        	bestTitle = contentCount.getKey();
	        }
        }
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found title candidates: {}", entries);
			LOG.debug("Best title candidate: {}", bestTitle);
		}
		
		//mark all text blocks before title as no content
		for (TextBlock tb : doc.getTextBlocks().subList(0, bestTitle + 1)) {
			tb.setIsContent(false);
		}
		//find first text block labeled "INDICATES_END_OF_TEXT" of "TITLE"
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
