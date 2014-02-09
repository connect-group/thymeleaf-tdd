package com.connect_group.thymeleaf.testing.hamcrest;

import java.util.Collection;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class Exists extends BaseMatcher<Object> {
	public Exists() {}
	
	@Override
	public boolean matches(Object obj) {
		boolean exists = false;
		if(obj!=null) {
			if(obj instanceof Collection) {
				exists = !((Collection<?>)obj).isEmpty();
			} else {
				exists = true;
			}
		}
		return exists;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("exists");
	}

}
