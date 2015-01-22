package org.cee.rest.article;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.cee.service.article.ArticleService;
import org.cee.store.EntityKey;
import org.cee.store.StoreException;
import org.cee.store.article.ArticleKey;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Path("article")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ArticleResource {

	private ArticleService articleService;
	
	@Required
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	@GET
	@Path("{siteKeys}")
	public List<ArticleKey> orderedByName(@PathParam("siteKeys") String siteKeys) throws StoreException {
		return articleService.articlesOfSites(EntityKey.fromCommaSeparatedList(siteKeys));
	}
	
	@GET
	@Path("ofWorkingSet/{workingSetKey}")
	public List<ArticleKey> ofWorkingSet(@PathParam("workingSetKey") String workingSetKey) throws StoreException {
		return articleService.articlesOfWorkingSet(EntityKey.get(workingSetKey));
	}
}
