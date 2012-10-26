package com.cee.news.parser.impl;

import java.util.List;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

/**
 * Finds text blocks which match given texts and marks them with label
 * 
 */
public class MatchingTextFinder implements BoilerpipeFilter {
	
	private final String label;

	private final List<String> stopText;
	
	private final int maxNumberOfWords;
	
	private int minLength = 0;
	
	public MatchingTextFinder(String label, List<String> stopText, int maxNumberOfWords) {
		this.label = label;
		this.stopText = stopText;
		this.maxNumberOfWords = maxNumberOfWords;
		for (String content : stopText) {
			if (minLength > content.length()) {
				minLength = content.length();
			}
		}
	}

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		boolean changes = false;
		for (TextBlock tb : doc.getTextBlocks()) {
			final int numWords = tb.getNumWords();
			if (numWords < maxNumberOfWords) {
				final String text = tb.getText().trim();
				final int len = text.length();
				if (len >= minLength) {
					final String textLC = text.toLowerCase();
					for (String stop : stopText) {
						if (textLC.contains(stop)) {
							tb.addLabel(label);
							changes = true;
						}
					}
				}
			}
		}
		return changes;
	}

}
