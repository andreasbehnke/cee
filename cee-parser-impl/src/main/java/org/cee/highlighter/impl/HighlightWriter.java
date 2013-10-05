package org.cee.highlighter.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.cee.highlighter.ContentHighlighter.Settings;
import org.cee.news.model.ContentExtractionMetaData;
import org.cee.news.model.TextBlock;
import org.xml.sax.Attributes;

public class HighlightWriter {

	private final Writer output;
	
	private final TemplateCache templateCache;
	
	private final Map<String, String> parameters;
	
	private final Settings settings;
	
	private final ContentExtractionMetaDataFormatter formatter = new ContentExtractionMetaDataFormatter();

	public HighlightWriter(final Writer output, final TemplateCache templateCache, final Settings settings) {
		this.output = output;
		this.templateCache = templateCache;
		this.settings = settings;
		parameters = new HashMap<String, String>();
		String namespace = RandomStringUtils.random(7, true, false);
		parameters.put(Settings.PARAMETER_NAMESPACE, namespace);
	}
	
	public void write(char[] ch, int start, int length) {
		try {
	        output.write(ch, start, length);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

	public void write(String text) {
		try {
	        output.write(text);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public void writeStartTag(String qname, Attributes attributes, String[] attributeOverrides) {
		try {
	        output.append('<')
	        	.append(qname);
	        for (int i = 0; i < attributes.getLength(); i++) {
				final String attr = attributes.getQName(i);
				String value = null;
				if (attributeOverrides != null) {
					value = attributeOverrides[i];
				}
				if (value == null) {
					value = attributes.getValue(i);
				}
				output.append(' ')
					.append(attr)
					.append("=\"")
					.append(value)
					.append("\"");
			}
	        output.append('>');
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}
	
	public void writeEndTag(String qname) {
		try {
	        output.append("</")
	        	.append(qname)
	        	.append('>');
        } catch (IOException e) {
	        throw new RuntimeException(e);
        }
	}
	
	private void writeTemplate(String template) {
		write(templateCache.fillTemplate(template, parameters));
	}
	
	public void writeHeaderTemplate() {
		writeTemplate(settings.getHeaderTemplate());
	}
	
	public void writeIssueContainerTemplate(List<String> issues) {
		
	}
	
	public void writeMetadataContainerTemplate(List<TextBlock> textBlocks) {
		StringBuilder content = new StringBuilder();
		for (TextBlock textBlock : textBlocks) {
			ContentExtractionMetaData metaData = textBlock.getMetaData();
	        parameters.put(Settings.PARAMETER_CONTENT, formatter.format(metaData));
	        parameters.put(Settings.PARAMETER_ID, metaData.getId() + "");
	        content.append(templateCache.fillTemplate(settings.getMetadataTemplate(), parameters));
        }
		parameters.put(Settings.PARAMETER_CONTENT, content.toString());
		writeTemplate(settings.getMetadataContainerTemplate());
	}
	
	public void writeMetadataIconTemplate(ContentExtractionMetaData metaData) {
		parameters.put(Settings.PARAMETER_ID, metaData.getId() + "");
		writeTemplate(settings.getMetadataIconTemplate());
	}
	
	public void writeContentBlockTemplate(String content) {
		parameters.put(Settings.PARAMETER_CONTENT, content);
		writeTemplate(settings.getContentBlockTemplate());
	}
}