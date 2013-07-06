package com.cee.news.client.content;

import java.io.IOException;

import com.cee.news.model.EntityKey;
import com.google.gwt.text.shared.Renderer;

public class EntityKeyRenderer implements Renderer<EntityKey> {

	@Override
	public String render(EntityKey object) {
		if (object == null) {
			return "Bitte Auswählen"; //TODO localization
		} else {
			return object.getName();
		}
	}

	@Override
	public void render(EntityKey object, Appendable appendable)
			throws IOException {
		appendable.append(render(object));
	}

}
