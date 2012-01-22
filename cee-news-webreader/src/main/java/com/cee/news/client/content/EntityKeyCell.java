package com.cee.news.client.content;

import com.cee.news.client.util.SafeHtmlUtil;
import com.cee.news.model.EntityKey;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.cell.client.Cell;

public class EntityKeyCell extends AbstractCell<EntityKey> {
	@Override
	public void render(Cell.Context context, EntityKey value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(SafeHtmlUtil.sanitize(value.getHtmlContent()));
		}
	}
}
