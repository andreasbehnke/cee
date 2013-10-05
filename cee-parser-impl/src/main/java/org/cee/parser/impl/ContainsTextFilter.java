package org.cee.parser.impl;

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
