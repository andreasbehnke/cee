package org.cee.store.lucene;

/*
 * #%L
 * Content Extraction Engine - News Store Lucene
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

import java.util.Calendar;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DateTools.Resolution;

public class DocumentBuilder {

	private Document document;
	
	public DocumentBuilder() {
		document = new Document();
	}
	
	public DocumentBuilder(Document document) {
		this.document = document;
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
	
	public DocumentBuilder addTextFieldWithTermVectors(String name, String value, Field.Store store) {
		if (value != null) {
			FieldType type = new FieldType();
			type.setStored(store == Store.YES);
			type.setTokenized(true);
			type.setIndexed(true);
			type.setStoreTermVectors(true);
			type.freeze();
			document.add(new Field(name, value, type));
		}
		return this;
	}
	
	public DocumentBuilder addStoredField(String name, String value) {
		if (value != null) {
			document.add(new StoredField(name, value));
		}
		return this;
	}
	
	public DocumentBuilder addDateField(String name, Calendar value, Field.Store store) {
		return addStringField(name, DateTools.dateToString(value.getTime(), Resolution.MINUTE), store);
	}
	
	public DocumentBuilder removeField(String name) {
		document.removeField(name);
		return this;
	}
}
