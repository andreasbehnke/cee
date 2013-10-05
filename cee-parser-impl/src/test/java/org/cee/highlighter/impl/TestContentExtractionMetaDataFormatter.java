package org.cee.highlighter.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.cee.highlighter.impl.ContentExtractionMetaDataFormatter;
import org.cee.news.model.ContentExtractionMetaData;
import org.cee.news.model.ContentExtractionMetaData.Property;
import org.junit.Test;

public class TestContentExtractionMetaDataFormatter {

	@Test
	public void testFormat() {
		ContentExtractionMetaDataFormatter formatter = new ContentExtractionMetaDataFormatter();
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property("abc", "def"));
		List<String> values = new ArrayList<String>();
		values.add("x1");
		values.add("x2");
		properties.add(new Property("ghi", values));
		properties.add(new Property("null", (String)null));
		BitSet containedTextBlocks = new BitSet();
		containedTextBlocks.set(123);
		containedTextBlocks.set(456);
		ContentExtractionMetaData metaData = new ContentExtractionMetaData(23, true, properties, containedTextBlocks);
		
		String formatted = formatter.format(metaData);
		assertEquals("<dl><dt>id</dt><dd>23</dd>" +
				"<dt>is content</dt><dd>true</dd>" +
				"<dt>abc</dt><dd>def</dd>" +
				"<dt>ghi</dt><dd>x1</dd><dd>x2</dd>" +
				"<dt>null</dt><dd>null</dd>" +
				"<dt>contained text blocks</dt><dd>{123, 456}</dd></dl>", formatted);
	}

}
