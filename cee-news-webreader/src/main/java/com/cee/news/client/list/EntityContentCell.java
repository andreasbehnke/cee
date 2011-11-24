package com.cee.news.client.list;

import com.cee.news.client.util.SafeHtmlUtil;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.cell.client.Cell;

public class EntityContentCell extends AbstractCell<EntityContent> {
	@Override
	public void render(Cell.Context context, EntityContent value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(SafeHtmlUtil.sanitize(value.getHtmlContent()));
		}
	}
}
