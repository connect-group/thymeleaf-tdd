package com.connect_group.thymeleaf.testing.hamcrest;


import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;



public class OccursOnce extends TypeSafeMatcher<Collection<?>> {
	public OccursOnce() {}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("occurs once");
		
	}

	@Override
	public boolean matchesSafely(Collection<?> items) {
		return items!=null && items.size()==1;
	}
	
}
