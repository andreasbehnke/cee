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


import java.util.List;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

public class ContainsTextFilter implements BoilerpipeFilter {

	private final String label;

	private final List<String> matches;

	private final int maxNumWords;
	
	private final boolean stopAtFirstMatch;

	public ContainsTextFilter(String label, List<String> matches, int maxNumWords, boolean stopAtFirstMatch) {
		this.label = label;
		this.matches = matches;
		this.maxNumWords = maxNumWords;
		this.stopAtFirstMatch = stopAtFirstMatch;
	}
	
	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		boolean changes = false;
		for (TextBlock tb : doc.getTextBlocks()) {
			if (tb.getNumWords() <= maxNumWords) {
				String text = tb.getText().trim().toLowerCase();
				for (String match : matches) {
					if (text.contains(match)) {
						tb.addLabel(label);
						changes = true;
						if (stopAtFirstMatch) {
							return true;
						}
						break;
					}
				}
			}
		}
		return changes;
	}

}
