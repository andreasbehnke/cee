package com.cee.news.server.content.renderer;

import com.cee.news.client.content.EntityContent;
import com.cee.news.model.EntityKey;
import com.cee.news.model.Site;

public class SiteContentRenderer extends DefaultContentRenderer<EntityKey, Site> {

    @Override
    public EntityContent<EntityKey> render(EntityKey key, Site site, String templateName) {
        if (key == null) {
            throw new IllegalArgumentException("Parameter key must not be null");
        }
        if (site == null) {
            throw new IllegalArgumentException("Parameter site must not be null");
        }
        StringBuilder builder = new StringBuilder();
        String title = site.getTitle();
        String description = site.getDescription();
        builder.append("<p>").append(title == null ? site.getLocation() : title).append("</p>")
        .append("<p>").append(site.getName()).append("</p>");
        if (description != null) {
            builder.append("<p>").append(site.getDescription()).append("</p>");
        }
        return new EntityContent<EntityKey>(key, builder.toString());
    }
}
