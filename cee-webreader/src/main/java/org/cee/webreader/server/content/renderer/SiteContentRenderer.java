package org.cee.webreader.server.content.renderer;

import org.cee.news.model.EntityKey;
import org.cee.news.model.Site;
import org.cee.webreader.client.content.EntityContent;

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
//        String title = site.getTitle();
//        String description = site.getDescription();
//        builder.append(title == null ? site.getLocation() : title);
        builder.append(site.getName());
//        if (description != null) {
//            builder.append("<p>").append(site.getDescription()).append("</p>");
//        }
        return new EntityContent<EntityKey>(key, builder.toString());
    }
}
