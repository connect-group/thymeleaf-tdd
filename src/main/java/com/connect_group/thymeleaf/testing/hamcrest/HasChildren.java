package com.connect_group.thymeleaf.testing.hamcrest;

import com.connect_group.thymesheet.query.HtmlElement;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HasChildren extends TypeSafeMatcher<HtmlElement> {

  @Override
  public void describeTo(Description description) {
    description.appendText("has children");

  }

  @Override
  public boolean matchesSafely(HtmlElement item) {
    return item.getElement().hasChildren();
  }


}
