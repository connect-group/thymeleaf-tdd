package com.connect_group.thymeleaf.testing.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.connect_group.thymesheet.query.HtmlElement;
import com.connect_group.thymesheet.query.HtmlElements;

public class IsSingleElementThat extends TypeSafeMatcher<HtmlElements> {
	private final Matcher<HtmlElement> individualMatcher;
	
	public IsSingleElementThat(Matcher<HtmlElement> individualMatcher) {
		this.individualMatcher = individualMatcher;
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("is single element that ");
		individualMatcher.describeTo(description);
	}

	@Override
	public boolean matchesSafely(HtmlElements elements) {
		return elements.size()==1 && individualMatcher.matches(elements.get(0));
	}


}
