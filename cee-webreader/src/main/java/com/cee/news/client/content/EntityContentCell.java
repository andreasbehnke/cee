package com.cee.news.client.content;

import com.cee.news.client.util.SafeHtmlUtil;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class EntityContentCell<K> extends AbstractCell<EntityContent<K>> {
	@Override
	public void render(Cell.Context context, EntityContent<K> value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(SafeHtmlUtil.sanitize(value.getContent()));
		}
	}
}
