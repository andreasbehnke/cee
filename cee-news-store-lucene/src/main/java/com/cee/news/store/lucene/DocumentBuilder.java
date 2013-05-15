package com.cee.news.store.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class DocumentBuilder {

	private Document document;
	
	public DocumentBuilder() {
		document = new Document();
	}

	public Document getDocument() {
		return document;
	}
	
	public DocumentBuilder addStringField(String name, String value, Field.Store store) {
		if (value != null) {
			document.add(new StringField(name, value, store));
		}
		return this;
	}
	
	public DocumentBuilder addTextField(String name, String value, Field.Store store) {
		if (value != null) {
			document.add(new TextField(name, value, store));
		}
		return this;
	}
	
	public DocumentBuilder addStoredField(String name, String value) {
		if (value != null) {
			document.add(new StoredField(name, value));
		}
		return this;
	}
}
