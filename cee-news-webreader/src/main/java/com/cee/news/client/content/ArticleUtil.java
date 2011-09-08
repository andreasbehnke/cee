package com.cee.news.client.content;

public class ArticleUtil {

	//TODO: Warning, this depends on the concrete ArticleStore implementation JcrArticleStore!
	public static String getSiteKeyFromArticleKey(String articleKey) {
		return articleKey.substring(0, articleKey.indexOf('/'));
	}
}