package com.connect_group.thymeleaf.testing.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.connect_group.thymesheet.query.HtmlElement;
import com.connect_group.thymesheet.query.HtmlElements;

public class HasTextInOrder extends TypeSafeMatcher<HtmlElements> {
	private final String[] values;
	
	public HasTextInOrder(String[] values) {
		this.values = values;
	}
	
	@Override
	public void describeMismatchSafely(HtmlElements items, Description description) {
		if (items.size()!=values.length) {
			description.appendText(items.size() + " elements with text [");
		} else {
			description.appendText("[");
		}
		
		boolean first = true;
		for(HtmlElement item : items) {
			if(!first) description.appendText(", ");
			description.appendValue(item.text());
			first = false;
		}
		description.appendText("]");
	}

	@Override
	protected boolean matchesSafely(HtmlElements items) {
		if (items.size()==values.length) {
			boolean matches=true;
			for(int i=0; i<values.length && matches; i++) {
				HtmlElement item = items.get(i);
				matches &= new HasOnlyText(values[i]).matchesSafely(item);
			}
			return matches;
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(values.length + " elements with text ").appendValue(values);
	}

}
