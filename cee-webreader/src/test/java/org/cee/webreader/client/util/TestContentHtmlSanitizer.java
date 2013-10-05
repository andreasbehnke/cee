package org.cee.webreader.client.util;

import java.util.Arrays;

import org.cee.webreader.client.util.ContentHtmlSanitizer;
import org.junit.Assert;
import org.junit.Test;

public class TestContentHtmlSanitizer {

	@Test
	public void testSanitize() {
		String input = "this is an broken tag><b class=\"bclass\" id=\"hjkhg\">bold</b >";
		input += "<em class='single quotes'>em</em>";
		input += "<i class='single quotes'>i</i>";
		input += "<hr class='single quotes'>hr</hr>";
		input += "<ul class='single quotes'>ul</ul>";
		input += "<p class=\"p-class\"><ol class='single quotes'><li class=\"\">li</li></ol></p    >";
		input += "<h1 class=\"h-class\">H1</h1>";
		input += "<h2 class=\"h-class\">H2</h2>";
		input += "<h3 class=\"h-class\" broken stuff should not raise execption>H3 This should be encoded:>>&&amp;</h3>";
		input += "<H4 class=\"h-class\">H4</H4>";
		input += "<h5 class=\"h-class\">H5</h5>";
		input += "<h6 class=\"h-class\">H6</h6>";
		input += "<a id='not allowed' href=\"test.html\" class=\"link-class\" target=\"target\">link text</a>";
		input += "<img src=\"image.png\" class=\"image-class\"><this is an broken=\"tag\"";
		input += "<br><br/><br class=\"break class\"><br class=\"break class\"/>";
		
		String expected = "this is an broken tag&gt;<b class=\"bclass\">bold</b>";
		expected += "<em class=\"single quotes\">em</em>";
		expected += "<i class=\"single quotes\">i</i>";
		expected += "<hr class=\"single quotes\">hr</hr>";
		expected += "<ul class=\"single quotes\">ul</ul>";
		expected += "<p class=\"p-class\"><ol class=\"single quotes\"><li class=\"\">li</li></ol></p>";
		expected += "<h1 class=\"h-class\">H1</h1>";
		expected += "<h2 class=\"h-class\">H2</h2>";
		expected += "<h3 class=\"h-class\">H3 This should be encoded:&gt;&gt;&amp;&amp;</h3>";
		expected += "<h4 class=\"h-class\">H4</h4>";
		expected += "<h5 class=\"h-class\">H5</h5>";
		expected += "<h6 class=\"h-class\">H6</h6>";
		expected += "<a href=\"test.html\" class=\"link-class\" target=\"target\">link text</a>";
		expected += "<img src=\"image.png\" class=\"image-class\">&lt;this is an broken=&quot;tag&quot;";
		expected += "<br><br><br class=\"break class\"><br class=\"break class\">";
		
		String result = new ContentHtmlSanitizer(true).sanitize(input).asString();
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testAppendAttributes() {
		ContentHtmlSanitizer sanitizer = new ContentHtmlSanitizer(true);
		StringBuilder sanitized = new StringBuilder();
		
		sanitizer.appendAttributes(sanitized, "a=\"a value\" ab ='another' abc= \"another\" cde=\"cde\"", Arrays.asList("ab","cde"));
		Assert.assertEquals(" ab=\"another\" cde=\"cde\"", sanitized.toString());
	}
	
	@Test
	public void testEscapeIllegalTags() {
		String input = "<div>Test123</div>";
		String expected = "&lt;div&gt;Test123&lt;/div&gt;";
		String result = new ContentHtmlSanitizer(true).sanitize(input).asString();
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testRemoveIllegalTags() {
		String input = "<div>Test123</div>";
		String expected = "Test123";
		String result = new ContentHtmlSanitizer(false).sanitize(input).asString();
		Assert.assertEquals(expected, result);
	}
}
