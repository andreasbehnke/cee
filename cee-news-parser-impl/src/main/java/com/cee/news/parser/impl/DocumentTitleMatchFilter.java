package com.cee.news.parser.impl;

import de.l3s.boilerpipe.BoilerpipeFilter;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

public class DocumentTitleMatchFilter implements BoilerpipeFilter {
	
	private final String label;
	
	private final double maxDistance;
	
	private final String match;
	
	private final boolean stopAtFirstMatch;
	
	public DocumentTitleMatchFilter(String label, String match, double maxDistance, boolean stopAtFirstMatch) {
		this.label = label;
		this.match = match;
		this.maxDistance = maxDistance;
		this.stopAtFirstMatch = stopAtFirstMatch;
	}

	@Override
	public boolean process(TextDocument doc) throws BoilerpipeProcessingException {
		boolean changes = false;
		for (TextBlock tb : doc.getTextBlocks()) {
			String text = tb.getText();
			if (text.indexOf(':') > 0) {
				text = text.substring(text.indexOf(':') + 1);
			}
			if (Levenshtein.normalizedDistance(text, match) < maxDistance) {
				tb.addLabel(label);
				changes = true;
			} else if(text.length() < match.length() * 2 && text.endsWith(match)) {
				tb.addLabel(label);
				changes = true;
			}
			if (changes & stopAtFirstMatch) {
				break;
			}
		}
		return changes;
	}

}
