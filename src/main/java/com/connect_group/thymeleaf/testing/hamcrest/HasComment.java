package com.connect_group.thymeleaf.testing.hamcrest;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.query.HtmlElement;

public class HasComment extends TypeSafeMatcher<HtmlElement> {
	private final String expectedText;
	
	public HasComment() {
		this.expectedText = null;
	}
	
	public HasComment(String expectedText) {
		this.expectedText = expectedText;
	}

	@Override
	public void describeTo(Description description) {
		if(expectedText == null) {
			description.appendText("has at least one comment");
		} else {
			description.appendText("has a comment <!--").appendValue(expectedText).appendText("-->");
		}
	}

	@Override
	protected boolean matchesSafely(HtmlElement item) {		
		Element element = item.getElement();
		if(element!=null && element.hasChildren()) {
			List<Node> children = element.getChildren();
		
			for(Node child : children) {
				if(child instanceof Comment) {
					if(expectedText==null) {
						return true;
					} else {
						if(expectedText.equals(((Comment)child).getContent())) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
}
