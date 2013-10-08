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


import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

public class DocumentTitleMatchFilter implements BoilerpipeFilter {
	
	private final String label;
	
	private final double maxDistance;
	
	private final String title;
	
	private final boolean titleContainsColon;
	
	private String titleBeforeColon;
	
	private String titleAfterColon;
	
	private final boolean stopAtFirstMatch;
	
	public DocumentTitleMatchFilter(String label, String title, double maxDistance, boolean stopAtFirstMatch) {
		if (title == null) {
			throw new IllegalArgumentException("Parameter title must not be null");
		}
		this.label = label;
		this.title = title;
		this.maxDistance = maxDistance;
		this.stopAtFirstMatch = stopAtFirstMatch;
		int indexOfColon = title.indexOf(':');
		if (indexOfColon > 0) {
			titleContainsColon = true;
			titleBeforeColon = title.substring(0, indexOfColon).trim();
			titleAfterColon = title.substring(indexOfColon + 1).trim();
		} else {
			titleContainsColon = false;
		}
	}
	
	private boolean checkText(TextBlock tb, String text, String expression) {
		if (Levenshtein.normalizedDistance(text, expression) < maxDistance) {
			tb.addLabel(label);
			return true;
		}
		return false;
	}

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		if (title == null) {
			return false;
		}
		boolean changes = false;
		for (TextBlock tb : doc.getTextBlocks()) {
			String text = tb.getText();
			if (!checkText(tb, text, title)) {
				int indexOfColon = text.indexOf(':');
				if(indexOfColon > 0) {
					//text contains colon. Test the string before colon and after colon separately.
					String beforeColon = text.substring(0, indexOfColon).trim();
					String afterColon = text.substring(indexOfColon + 1).trim();
					if (titleContainsColon) {
						if (!checkText(tb, beforeColon, titleBeforeColon)) {
							changes |= checkText(tb, afterColon, titleAfterColon);
						}
					} else {
						if (!checkText(tb, beforeColon, title)) {
							changes |= checkText(tb, afterColon, title);
						}
					}
				} else if (titleContainsColon) {
					//check if text matches before / after colon
					boolean isTextBeforeColon = checkText(tb, text, titleBeforeColon);
					changes |= isTextBeforeColon;
					if (!isTextBeforeColon) {
						changes |= checkText(tb, text, titleAfterColon);
					}
				}
			} else {
				changes = true;
			}
			if (changes & stopAtFirstMatch) {
				break;
			}
		}
		return changes;
	}

}
